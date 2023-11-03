package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.enums.live.RoomPlayRecordStatusEnum;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.live.api.message.RoomPlayRecordMessage;
import com.cmnt.dbpick.live.api.params.RoomPlayRecordParams;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomPlayRecord;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomPlayRecordRepository;
import com.cmnt.dbpick.live.server.service.RoomPlayRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 房间播放记录
 */
@Slf4j
@Service
public class RoomPlayRecordServiceImpl implements RoomPlayRecordService {



    @Autowired
    private StreamingRoomPlayRecordRepository roomPlayRecordRepository;

    /**
     * 保存直播开始记录
     */
    @Override
    public Boolean savePlayStartRecord(RoomPlayRecordParams params) {
        log.info("保存直播开始记录, param={}",params);
        StreamingRoomPlayRecord playRecord = new StreamingRoomPlayRecord();
        FastBeanUtils.copy(params,playRecord);
        playRecord.setStatus(RoomPlayRecordStatusEnum.START.getStatus());
        playRecord.setStopTime(params.getStartTime());
        playRecord.initSave(params.getCreateUser());
        log.info("保存直播开始记录, data={}",playRecord);
        roomPlayRecordRepository.save(playRecord);
        return Boolean.TRUE;
    }


    /**
     * 更新直播结束记录
     */
    @Override
    public RoomPlayRecordMessage updatePlayEndRecord(RoomPlayRecordParams params) {
        log.info("更新直播结束记录, param={}",params);
        StreamingRoomPlayRecord playRecord =  roomPlayRecordRepository.findTop1ByRoomNoAndStatusOrderByCreateDateDesc(
                params.getRoomNo(), RoomPlayRecordStatusEnum.START.getStatus());
        if(Objects.isNull(playRecord)){
            log.info("未查询到保存直播开始记录, roomNo={}",params.getRoomNo());
            playRecord = new StreamingRoomPlayRecord();
            String startTime = DateUtil.dateFormatter(
                    DateUtil.str2Date(params.getStopTime(), DateUtil.Y_M_D_HMS),
                    DateUtil.Y_M_D_000);
            log.info("设置开始时间为结束时间当天的 00:00:00, startTime={}",startTime);
            FastBeanUtils.copy(params,playRecord);
            playRecord.setStartTime(startTime);
            playRecord.initSave(params.getCreateUser());
        }
        playRecord.setStopTime(params.getStopTime());
        playRecord.setStatus(RoomPlayRecordStatusEnum.STOP.getStatus());
        playRecord.initUpdate(params.getCreateUser());
        log.info("更新直播结束记录, data={}",playRecord);
        roomPlayRecordRepository.save(playRecord);
        RoomPlayRecordMessage msg = RoomPlayRecordMessage.builder().roomNo(playRecord.getRoomNo())
                .startTime(playRecord.getStartTime()).stopTime(playRecord.getStopTime()).build();
        return msg;
    }
}
