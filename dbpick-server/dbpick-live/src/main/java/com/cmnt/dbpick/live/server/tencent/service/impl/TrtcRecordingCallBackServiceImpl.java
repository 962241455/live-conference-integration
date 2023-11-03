package com.cmnt.dbpick.live.server.tencent.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.enums.live.VideoRecordTaskStatusEnum;
import com.cmnt.dbpick.common.tx.tencent.response.trtc.RecordCallbackResponse;
import com.cmnt.dbpick.common.tx.tencent.response.trtc.eventInfo.RecordCallbackEvent;
import com.cmnt.dbpick.common.tx.tencent.response.trtc.eventInfo.RecordCallbackEventTencentVod;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.RedisUtils;
import com.cmnt.dbpick.live.api.params.RoomVideoEditParam;
import com.cmnt.dbpick.live.server.mongodb.document.CloudRecordingTaskLog;
import com.cmnt.dbpick.live.server.service.CallbackService;
import com.cmnt.dbpick.live.server.service.LiveRecordTaskLogService;
import com.cmnt.dbpick.live.server.service.LiveVideosService;
import com.cmnt.dbpick.live.server.tencent.enums.TrtcCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.event.TrtcHandlerEventType;
import com.cmnt.dbpick.live.server.tencent.service.TrtcCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * 实时音视频 云端录制 回调
 */
@Slf4j
@Component
@TrtcHandlerEventType(TrtcCallBackEnum.TRTC_RECORDING)
public class TrtcRecordingCallBackServiceImpl implements TrtcCallBackService {

    /**  云端录制事件组 对应 事件类型 */
    public final static String EVENT_TYPE_RECORDING_RECORDER_START = "301";//	云端录制录制模块启动
    public final static String EVENT_TYPE_RECORDING_RECORDER_STOP = "302";//	云端录制录制模块退出
    public final static String EVENT_TYPE_RECORDING_FAILOVER = "306";//	云端录制发生迁移，原有的录制任务被迁移到新负载上时触发
    public final static String EVENT_TYPE_RECORDING_DOWNLOAD_IMAGE_ERROR = "309";//	云端录制下载解码图片文件发生错误
    public final static String EVENT_TYPE_RECORDING_VOD_COMMIT = "311";//	云端录制 VOD 录制任务上传媒体资源完成
    public final static String EVENT_TYPE_RECORDING_VOD_STOP = "312";//	云端录制 VOD 录制任务结束
    /**
     * 311-云端录制 VOD 录制任务上传媒体资源完成
     * 0：代表本录制文件正常上传至点播平台
     * 1：代表本录制文件滞留在服务器或者备份存储上
     * 2：代表本录制文件上传点播任务异常
     */
    public final static Integer EVENT_INFO_PAYLOAD_STATUS_SUCCESS = 0;
    public final static Integer EVENT_INFO_PAYLOAD_STATUS_WAIT = 1;
    public final static Integer EVENT_INFO_PAYLOAD_STATUS_ERROR = 2;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CallbackService callbackServiceImpl;

    @Autowired
    private LiveRecordTaskLogService liveRecordTaskLogServiceImpl;
    @Autowired
    private LiveVideosService liveVideosServiceImpl;

    @Override
    public void execute(String jsonObjectStr) {
        log.info("实时音视频 云端录制 回调返回参数 str={}", jsonObjectStr);
        RecordCallbackResponse res = JSON.parseObject(jsonObjectStr, RecordCallbackResponse.class);
        log.info("实时音视频 云端录制 回调返回参数 response={}", res);
        String roomNo = res.getEventInfo().getRoomId();
        String ak = "";
        String thirdId = "";
        String operator = "";
        switch (res.getEventType()){
            case EVENT_TYPE_RECORDING_RECORDER_START:
                log.info("云端录制 VOD 录制任务启动: roomNo={}",roomNo);
                break;
            case EVENT_TYPE_RECORDING_RECORDER_STOP:
                log.info("云端录制 VOD 录制任务停止: roomNo={}",roomNo);
                break;
            case EVENT_TYPE_RECORDING_VOD_COMMIT :
                log.info("云端录制 VOD 录制任务上传媒体资源完成 roomNo={}",roomNo);
                CloudRecordingTaskLog taskLog = handleCommitVOD(res);
                if(Objects.nonNull(taskLog)){
                    ak = taskLog.getAk();
                    thirdId = taskLog.getThirdId();
                    operator = taskLog.getCreateUser();
                }
                break;
            case EVENT_TYPE_RECORDING_VOD_STOP:
                log.info("云端录制 VOD 录制任务结束: roomNo={}",roomNo);
                break;
            default:
                break;
        }
        callbackServiceImpl.saveTrtcCallbackLog(ak,thirdId,operator,res);
    }

    /**
     * 录制完成，开始上传文件
     * @param res
     * @return
     */
    private CloudRecordingTaskLog handleCommitVOD(RecordCallbackResponse res){
        RecordCallbackEvent eventInfo = res.getEventInfo();
        CloudRecordingTaskLog task = liveRecordTaskLogServiceImpl.findLogByTaskId(eventInfo.getTaskId());
        if(Objects.isNull(task)){
            log.error("未查询到录制任务信息 ....");
            return task;
        }
        if(!Objects.equals(eventInfo.getPayload().getStatus(),EVENT_INFO_PAYLOAD_STATUS_SUCCESS)){
            log.error("录制文件正常上传至点播平台失败....");
            task.setStatus(VideoRecordTaskStatusEnum.DEFAULT.getValue());
            liveRecordTaskLogServiceImpl.updateTaskLog(task);
            return task;
        }

        log.info("录制文件上传至点播平台成功， 组装录制媒体信息保存到本地......");
        String roomNo = eventInfo.getRoomId();
        RecordCallbackEventTencentVod txVod = eventInfo.getPayload().getTencentVod();
        Long endTimeStamp = txVod.getEndTimeStamp();
        Long startTimeStamp = txVod.getStartTimeStamp();
        Long videoDurationMills = endTimeStamp-startTimeStamp;
        String fileName = String.format("%s_%s-%s.mp4",roomNo,
                DateUtil.fromDate2Str(new Date(startTimeStamp)),DateUtil.fromDate2Str(new Date(endTimeStamp)));
        RoomVideoEditParam param = RoomVideoEditParam.builder().fileId(txVod.getFileId()).fileName(fileName)
                .videoURL(txVod.getVideoUrl()).duration(DateUtil.formatMilliSecondToTimeStr(videoDurationMills))
                .seconds(new Long(videoDurationMills/1000).intValue()).build();
        String userId = eventInfo.getUserId();
        param.setThirdId(userId);
        param.setCreateUser(userId);
        log.info("组装录制媒体信息 param={}",param);
        liveVideosServiceImpl.saveRecordVideo(param, Boolean.FALSE);
        redisUtils.hmDel(RedisKey.LIVE_RECORD_TASK, roomNo);

        task.setStatus(VideoRecordTaskStatusEnum.SUCCESS.getValue());
        liveRecordTaskLogServiceImpl.updateTaskLog(task);
        return task;
    }


}
