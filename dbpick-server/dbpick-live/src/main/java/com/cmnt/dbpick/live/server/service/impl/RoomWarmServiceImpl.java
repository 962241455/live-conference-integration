package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.enums.PullStreamStatusEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.tx.tencent.TxCloudLiveUtil;
import com.cmnt.dbpick.common.tx.tencent.TxCloudVodUtil;
import com.cmnt.dbpick.common.tx.tencent.request.live.TxPushStreamModifyParam;
import com.cmnt.dbpick.common.tx.tencent.request.live.TxPushStreamParam;
import com.cmnt.dbpick.common.tx.tencent.response.TxPullStreamResponse;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.live.api.params.RoomVideoEditParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.vo.LiveOpenVo;
import com.cmnt.dbpick.live.api.vo.RoomWarmInfoVO;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoom;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomWarm;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomRepository;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomWarmRepository;
import com.cmnt.dbpick.live.server.service.RoomWarmService;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import com.cmnt.dbpick.live.server.service.TencentLiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 直播间信息配置
 */
@Slf4j
@Service
public class RoomWarmServiceImpl implements RoomWarmService {


    @Autowired
    private TxCloudVodUtil txCloudVodUtil;
    @Autowired
    private TxCloudLiveUtil txCloudLiveUtil;


    @Autowired
    private StreamingRoomRepository streamingRoomRepository;
    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;
    @Autowired
    private TencentLiveService tencentLiveService;

    @Autowired
    private StreamingRoomWarmRepository streamingRoomWarmRepository;

    /**
     * 暖场视频信息
     * @param param
     * @return
     */
    @Override
    public RoomWarmInfoVO info(StreamingRoomQueryParams param){
        String roomNo = param.getRoomNo();
        StreamingRoomWarm roomWarm = streamingRoomWarmRepository.findTop1ByRoomNoAndDeletedOrderByCreateDateDesc(
                roomNo,Boolean.FALSE);
        log.info("根据房间号查询暖场视频信息， roomNo={}, info={}",roomNo, roomWarm);
        RoomWarmInfoVO vo = new RoomWarmInfoVO();
        if(Objects.nonNull(roomWarm)){
            FastBeanUtils.copy(roomWarm,vo);
        }
        return vo;
    }

    /**
     * 保存暖场视频信息
     */
    @Override
    public RoomWarmInfoVO saveWarmVideo(RoomVideoEditParam param){
        String ak = streamingRoomServiceImpl.checkAccessKeyByThirdId(param.getThirdId());
        if (Objects.isNull(streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(param.getRoomNo()))){
            throw new BizException(ResponseCode.AUCTION_ILLEGAL_ROOM_NULL);
        }
        if(StringUtils.isBlank(param.getVideoURL()) || StringUtils.isBlank(param.getFileId())){
            throw new BizException(ResponseCode.NULL_VIDEO_URL);
        }
        StreamingRoomWarm data = new StreamingRoomWarm();
        FastBeanUtils.copy(param, data);
        data.setStatus(PullStreamStatusEnum.PAUSE.getValue());
        data.initSave(param.getCreateUser());
        data.setAk(ak);
        streamingRoomWarmRepository.save(data);
        RoomWarmInfoVO vo = new RoomWarmInfoVO();
        FastBeanUtils.copy(data,vo);
        return vo;
    }

    /**
     * 删除暖场视频信息
     * @param id
     * @param createUser 操作员
     * @return
     */
    @Override
    public Boolean deleteWarmVideo(String id,String createUser) {
        Optional<StreamingRoomWarm> warmVideo = streamingRoomWarmRepository.findById(id);
        if(!warmVideo.isPresent()){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        StreamingRoomWarm roomWarm = warmVideo.get();
        if(StringUtils.equals(roomWarm.getStatus(),PullStreamStatusEnum.ENABLE.getValue())){
            //提示 停止暖场视频
            throw new BizException(ResponseCode.WARM_VIDEO_ENABLE);
        }
        /*TxPullStreamResponse result = txCloudVodUtil.deleteMediaByFileId(roomWarm.getFileId());
        if(Objects.nonNull(result)){
            roomWarm.setTxRequestId(result.getRequestId());
        }*/
        roomWarm.initDel(createUser);
        streamingRoomWarmRepository.save(roomWarm);
        return Boolean.TRUE;
    }

    /**
     * 编辑暖场视频状态
     * @param param
     * @return
     */
    @Override
    public RoomWarmInfoVO editWarmVideoStatus(RoomVideoEditParam param) {
        Optional<StreamingRoomWarm> warmOptional = streamingRoomWarmRepository.findById(param.getId());
        if(StringUtils.isBlank(param.getId()) || !warmOptional.isPresent()){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        PullStreamStatusEnum statusEnum = PullStreamStatusEnum.getByValue(param.getStatus());
        StreamingRoomWarm warmVideo = warmOptional.get();
        switch (statusEnum){
            case ENABLE:
                StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(param.getRoomNo());
                if (Objects.isNull(room)){
                    throw new BizException(ResponseCode.AUCTION_ILLEGAL_ROOM_NULL);
                }
                log.info("创建暖场视频拉流转推任务");
                warmVideo.setStatus(PullStreamStatusEnum.ENABLE.getValue());
                LiveOpenVo liveOpen = tencentLiveService.liveOpen(param.getRoomNo());
                TxPushStreamParam reqParam = TxPushStreamParam.builder()
                        .startTime(DateUtil.fromDate2Str(new Date()))
                        //.endTime(room.getInfo().getStartTime())
                        .endTime(DateUtil.fromDate2Str(new Date(DateUtil.getTimeStrampSeconds()+10*60*1000)))
                        .urls(warmVideo.getVideoURL().split(",")).operator(param.getCreateUser())
                        .comment(warmVideo.getFileName()).build();
                reqParam.setStreamName(liveOpen.getSecret());
                TxPullStreamResponse result = txCloudLiveUtil.pullVodStreamTask(reqParam);
                warmVideo.setTxTaskId(result.getTaskId());
                warmVideo.setTxRequestId(result.getRequestId());
                warmVideo.initUpdate(param.getCreateUser());
                break;
            case PAUSE:
                log.info("删除暖场视频拉流转推任务");
                warmVideo.setStatus(PullStreamStatusEnum.PAUSE.getValue());
                TxPushStreamModifyParam reqDelParam = TxPushStreamModifyParam.builder()
                        .taskId(warmVideo.getTxTaskId()).operator(param.getCreateUser()).build();
                TxPullStreamResponse delResult = txCloudLiveUtil.delPullStreamTask(reqDelParam);
                if(Objects.nonNull(delResult)){
                    warmVideo.setTxRequestId(delResult.getRequestId());
                }
                warmVideo.initUpdate(param.getCreateUser());
                break;
            default:
                throw new BizException(PullStreamStatusEnum.UNKNOWN.getDesc());
        }
        streamingRoomWarmRepository.save(warmVideo);
        RoomWarmInfoVO vo = new RoomWarmInfoVO();
        FastBeanUtils.copy(warmVideo,vo);
        return vo;
    }



}
