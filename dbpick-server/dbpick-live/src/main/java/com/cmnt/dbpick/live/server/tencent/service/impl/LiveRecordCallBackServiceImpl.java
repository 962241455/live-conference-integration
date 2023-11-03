package com.cmnt.dbpick.live.server.tencent.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.enums.live.VideoRecordTaskStatusEnum;
import com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum;
import com.cmnt.dbpick.common.tx.tencent.response.live.LiveRecordCallBackResponse;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.live.api.params.RoomVideoEditParam;
import com.cmnt.dbpick.live.server.mongodb.document.CloudRecordingTaskLog;
import com.cmnt.dbpick.live.server.mongodb.document.LiveVideos;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveVideosRepository;
import com.cmnt.dbpick.live.server.service.*;
import com.cmnt.dbpick.live.server.tencent.enums.LiveCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.event.LiveHandlerEventType;
import com.cmnt.dbpick.live.server.tencent.service.LiveCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * 直播 录制回调
 */
@Slf4j
@Component
@LiveHandlerEventType(LiveCallBackEnum.LIVE_RECORD)
public class LiveRecordCallBackServiceImpl implements LiveCallBackService {

    @Autowired
    private LiveVideosRepository liveVideosRepository;

    @Autowired
    private CallbackService callbackServiceImpl;

    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;
    @Autowired
    private LiveRecordTaskLogService liveRecordTaskLogServiceImpl;
    @Autowired
    private LiveVideosService liveVideosServiceImpl;
    @Autowired
    private RoomPlaybackService roomPlaybackServiceImpl;


    @Override
    public void execute(String jsonObjectStr) {
        log.info("直播 录制 回调返回参数 str={}", jsonObjectStr);
        LiveRecordCallBackResponse res = JSON.parseObject(jsonObjectStr, LiveRecordCallBackResponse.class);
        log.info("直播 录制 回调返回参数 response={}", res);
        StreamingRoomVO roomInfo = streamingRoomServiceImpl.detail(res.getStream_id());
        if(Objects.isNull(roomInfo)){
            log.error("直播 录制 回调: 未查到房间信息 ....");
            callbackServiceImpl.saveLiveCallbackLog("","","", res);
            return;
        }
        String ak = roomInfo.getAk();
        String thirdId = roomInfo.getThirdId();
        String operator = roomInfo.getCreateUser();
        CloudRecordingTaskLog taskLog = saveRecordFileToVOD(res);
        if(Objects.nonNull(taskLog)){
            ak = taskLog.getAk();
            thirdId = taskLog.getThirdId();
            operator = taskLog.getCreateUser();
        }
        callbackServiceImpl.saveLiveCallbackLog(ak, thirdId, operator, res);
    }

    /**
     * 录制完成，保存文件信息
     * @param res
     * @return
     */
    private CloudRecordingTaskLog saveRecordFileToVOD(LiveRecordCallBackResponse res){
        String taskId = res.getTask_id();
        CloudRecordingTaskLog task = liveRecordTaskLogServiceImpl.findLogByTaskId(taskId);
        log.error("查询到录制任务信息 ....task={}",task);
        String userId = "";
        if(Objects.nonNull(task)){
            task.setStatus(VideoRecordTaskStatusEnum.SUCCESS.getValue());
            liveRecordTaskLogServiceImpl.updateTaskLog(task);
            userId = task.getCreateUser();
        }

        //log.info("录制文件上传至点播平台成功， 组装录制媒体信息保存到本地......");
        String roomNo = res.getStream_id();
        Long endTimeStamp = (res.getEnd_time())*1000;
        Long startTimeStamp = (res.getEnd_time() - res.getDuration())*1000;

        String fileName = String.format("%s_%s-%s",roomNo,
                DateUtil.fromDate2Str(new Date(startTimeStamp)), DateUtil.fromDate2Str(new Date(endTimeStamp)));
        RoomVideoEditParam param = RoomVideoEditParam.builder().fileId(res.getFile_id()).fileName(fileName)
                .duration(DateUtil.formatMilliSecondToTimeStr(res.getDuration().longValue()*1000))
                .seconds(res.getDuration()).videoURL(res.getVideo_url()).build();
        param.setRoomNo(roomNo);
        param.setCreateUser(userId);
        param.setThirdId(userId);
        log.info("组装录制媒体信息 param={}",param);
        LiveVideos liveVideos = liveVideosServiceImpl.saveRecordVideo(param, Boolean.FALSE);
        liveVideos.setTranscodeStatus(VideoTranscodeStatusEnum.NORMAL.getValue());
        liveVideosRepository.save(liveVideos);
        //redisUtils.hmDel(RedisKey.LIVE_RECORD_TASK, roomNo);
        roomPlaybackServiceImpl.saveRecordVideo(param);
        return task;
    }

}
