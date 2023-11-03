package com.cmnt.dbpick.stats.server.mq.service.impl;

import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.live.api.message.RoomPlayRecordMessage;
import com.cmnt.dbpick.live.api.params.RoomPlayRecordParams;
import com.cmnt.dbpick.stats.server.mq.event.SameDayEnum;
import com.cmnt.dbpick.stats.server.mq.event.SameDayHandlerEventType;
import com.cmnt.dbpick.stats.server.mq.service.RoomPlayRecordStatsService;
import com.cmnt.dbpick.stats.server.service.RoomInfoRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 根据直播记录统计直播数据 非同一天数据
 */
@Slf4j
@Component
@SameDayHandlerEventType(SameDayEnum.DIFF_DAY)
public class RoomPlayRecordDiffDayServiceImpl implements RoomPlayRecordStatsService {

    @Autowired
    private RoomInfoRecordService roomInfoRecordServiceImpl;

    @Override
    public void execute(RoomPlayRecordMessage record) {
        String recordStartTime = record.getStartTime();
        String recordStopTime = record.getStopTime();
        log.info("根据直播记录统计直播数据 直播时间 more day={}~{}",recordStartTime,recordStopTime);
        String playStartTime = DateUtil.addDateSecond(recordStartTime, -3 * 60);
        String playStopTime = DateUtil.addDateSecond(recordStopTime, 3 * 60);
        log.info("统计房间流量明细信息，根据直播时间前后冗余3分钟 search day={}~{}",playStartTime,playStopTime);
        List<DateUtil.CurrentDay> dayList = DateUtil.getDataStrList(playStartTime, playStopTime);
        dayList.forEach(
                dayStr -> {
                    RoomPlayRecordParams param = RoomPlayRecordParams.builder().roomNo(record.getRoomNo())
                            .startTime(dayStr.getCurrentDayMin()).stopTime(dayStr.getCurrentDayMax()).build();
                    roomInfoRecordServiceImpl.statsRoomPlayInfoDetail(param);
                    roomInfoRecordServiceImpl.statsRoomTRTCUsageInfo(param);
                }
        );
    }
}
