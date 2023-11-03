package com.cmnt.dbpick.stats.server.service.impl;

import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.page.PageUtils;
import com.cmnt.dbpick.common.tx.tencent.TxCloudLiveUtil;
import com.cmnt.dbpick.common.tx.tencent.TxCloudTrtcUtil;
import com.cmnt.dbpick.common.tx.tencent.request.live.TxStreamInfoParam;
import com.cmnt.dbpick.common.tx.tencent.request.trtc.TxRoomUsageInfoParam;
import com.cmnt.dbpick.common.tx.tencent.response.live.eventInfo.TxStreamPlayInfo;
import com.cmnt.dbpick.common.tx.tencent.response.trtc.RoomUsageInfoResponse;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.JacksonUtils;
import com.cmnt.dbpick.live.api.params.RoomPlayRecordParams;
import com.cmnt.dbpick.stats.api.params.QueryRoomStatsParams;
import com.cmnt.dbpick.stats.api.vo.LiveRoomStatsVO;
import com.cmnt.dbpick.stats.server.es.document.RoomInfoRecordIndex;
import com.cmnt.dbpick.stats.server.es.document.RoomTrtcUsageIndex;
import com.cmnt.dbpick.stats.server.es.repository.RoomInfoRecordEsRepository;
import com.cmnt.dbpick.stats.server.es.repository.RoomTrtcUsageEsRepository;
import com.cmnt.dbpick.stats.server.service.RoomInfoRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RoomInfoRecordServiceImpl implements RoomInfoRecordService {

    @Autowired
    private TxCloudLiveUtil txCloudLiveUtil;
    @Autowired
    private TxCloudTrtcUtil txCloudTrtcUtil;

    @Autowired
    private RoomInfoRecordEsRepository roomInfoRecordEsRepository;
    @Autowired
    private RoomTrtcUsageEsRepository roomTrtcUsageEsRepository;

    /**
     * 统计房间流量明细信息
     */
    @Override
    public Boolean statsRoomPlayInfoDetail(RoomPlayRecordParams param) {
        log.info("统计房间流量明细信息, param={}", param);
        if(Objects.isNull(param) || StringUtils.isBlank(param.getRoomNo())){
            return Boolean.FALSE;
        }
        List<RoomInfoRecordIndex> voList = new ArrayList<>();
        TxStreamInfoParam txParam = TxStreamInfoParam.builder()
                .startTime(param.getStartTime()).endTime(param.getStopTime()).build();
        txParam.setStreamName(param.getRoomNo());
        List<TxStreamPlayInfo> streamPlayInfo = txCloudLiveUtil.findStreamPlayInfo(txParam);
        streamPlayInfo.forEach(info -> voList.add(initRecord(info,param.getRoomNo())));
        roomInfoRecordEsRepository.saveAll(voList);
        log.info("统计房间流量明细信息到es, roomNo={}, size={}",param.getRoomNo(),voList.size());
        return Boolean.TRUE;
    }

    /**
     * 统计房间 trtc 通话分钟数据
     */
    @Override
    public Boolean statsRoomTRTCUsageInfo(RoomPlayRecordParams param) {
        log.info("统计房间 trtc 通话分钟数据, param={}", param);
        if(Objects.isNull(param) || StringUtils.isBlank(param.getRoomNo())){
            return Boolean.FALSE;
        }
        List<RoomTrtcUsageIndex> voList = new ArrayList<>();
        TxRoomUsageInfoParam txParam = TxRoomUsageInfoParam.builder()
                .startTime(param.getStartTime()).endTime(param.getStopTime()).build();
        txParam.setStreamName(param.getRoomNo());
        List<RoomUsageInfoResponse.RoomUsageInfo> usages = txCloudTrtcUtil.findRoomTRTCUsageInfo(txParam);
        usages.forEach(info -> voList.add(
                RoomTrtcUsageIndex.builder().id(info.getRoomString() + "_" + info.getCommId())
                        .roomNo(info.getRoomString()).commId(info.getCommId()).userId(info.getUserId())
                        .roomCreateTime(info.getCreateTime()).roomDestroyTime(info.getDestroyTime())
                        .isFinished(info.getFinished()).actTimeMillis(DateUtil.getTimeStrampSeconds())
                        .build()
        ));
        roomTrtcUsageEsRepository.saveAll(voList);
        log.info("统计房间 trtc 通话分钟数据到es, roomNo={}, size={}",param.getRoomNo(),voList.size());
        return Boolean.TRUE;
    }

    /**
     * 组装 房间流量明细vo
     */
    private RoomInfoRecordIndex initRecord(TxStreamPlayInfo info, String roomNo){
        RoomInfoRecordIndex roomInfoRecord = RoomInfoRecordIndex.builder().roomNo(roomNo)
                .id(roomNo + "_" + info.getTime()).sumFlux(info.getFlux().doubleValue())
                .recordTime(info.getTime()).recordTimeMillis(DateUtil.parseYMDHMS2Mils(info.getTime()))
                .build();
        Integer online = Objects.nonNull(info.getOnline())?info.getOnline():0;
        roomInfoRecord.setMaxOnLine(online);
        roomInfoRecord.setMinOnLine(online);
        return roomInfoRecord;
    }

    /**
     * 直播间流量明细数据
     * @param param
     * @return
     */
    @Override
    public PageResponse<LiveRoomStatsVO> roomFluxDetail(QueryRoomStatsParams param) {
        BoolQueryBuilder queryParam = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(param.getRoomNo())){
            queryParam.must(QueryBuilders.termQuery("roomNo",param.getRoomNo()));
        }
        Pageable page = PageRequest.of((param.getPageNum()-1),param.getPageSize(),
                Sort.Direction.DESC,"recordTimeMillis");
        Page<RoomInfoRecordIndex> data = roomInfoRecordEsRepository.search(queryParam, page);
        PageResponse<RoomInfoRecordIndex> response = PageUtils.handlePageToResponse(data);
        response.setPageNum(param.getPageNum());
        response.setPageSize(param.getPageSize());
        return JacksonUtils.toBean(JacksonUtils.toJson(response), PageResponse.class);
    }

    @Override
    public List<LiveRoomStatsVO> threeSecondRoomRecord(RoomNoParam param) {
        if (StringUtils.isBlank(param.getRoomNo())){
            log.error("房间号 不能为空");
            throw new BizException("房间号 不能为空");
        }
        List<LiveRoomStatsVO> voList = new ArrayList<>();
        String nowDateStr = DateUtil.nowDateTime(DateUtil.Y_M_D_HMS);
        TxStreamInfoParam txParam = TxStreamInfoParam.builder()
                .startTime(DateUtil.addDateSecond(nowDateStr,-3*60))
                .endTime(nowDateStr).build();
        txParam.setStreamName(param.getRoomNo());
        List<TxStreamPlayInfo> streamPlayInfo = txCloudLiveUtil.findStreamPlayInfo(txParam);
        streamPlayInfo.forEach(info -> voList.add(
                LiveRoomStatsVO.builder().roomNo(param.getRoomNo()).sumFlux(info.getFlux().doubleValue())
                        .recordTime(info.getTime()).recordTimeMillis(DateUtil.parseYMDHMS2Mils(info.getTime()))
                        .maxOnLine(Objects.nonNull(info.getOnline())?info.getOnline():0)
                        .minOnLine(Objects.nonNull(info.getOnline())?info.getOnline():0).build()
        ));
        return voList;
    }




}
