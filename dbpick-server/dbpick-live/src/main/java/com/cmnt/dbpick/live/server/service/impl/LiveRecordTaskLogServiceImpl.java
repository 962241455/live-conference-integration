package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.config.TxRecordConfig;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.enums.live.VideoRecordTaskStatusEnum;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.tx.tencent.TxCloudLiveUtil;
import com.cmnt.dbpick.common.tx.tencent.TxCloudTrtcUtil;
import com.cmnt.dbpick.common.tx.tencent.request.TxRecordTaskParam;
import com.cmnt.dbpick.common.tx.tencent.request.trtc.CloudRecordingParam;
import com.cmnt.dbpick.common.tx.tencent.response.TxPullStreamResponse;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.RedisUtils;
import com.cmnt.dbpick.common.utils.TxCloudUtil;
import com.cmnt.dbpick.live.api.vo.LiveOpenVo;
import com.cmnt.dbpick.live.api.vo.redis.LiveRecordTaskVO;
import com.cmnt.dbpick.live.server.mongodb.document.CloudRecordingTaskLog;
import com.cmnt.dbpick.live.server.mongodb.repository.CloudRecordingTaskLogRepository;
import com.cmnt.dbpick.live.server.service.LiveRecordTaskLogService;
import com.cmnt.dbpick.live.server.service.TencentLiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 云端录制任务记录
 * &
 * 直播录制任务
 */
@Slf4j
@Service
public class LiveRecordTaskLogServiceImpl implements LiveRecordTaskLogService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TxCloudUtil txCloudUtil;
    @Autowired
    private TxCloudLiveUtil txCloudLiveUtil;
    @Autowired
    private TxCloudTrtcUtil txCloudTrtcUtil;
    @Autowired
    private TencentLiveService tencentLiveServiceImpl;

    @Autowired
    private CloudRecordingTaskLogRepository cloudRecordingTaskLogRepository;

    /**
     * 根据任务id查询记录
     * @return
     */
    @Override
    public CloudRecordingTaskLog findLogByTaskId(String taskId) {
        log.info("根据任务id查询录制任务记录, taskId={}",taskId);
        return cloudRecordingTaskLogRepository.findTop1ByTxTaskIdOrderByCreateDateTimeDesc(taskId);
    }

    /**
     * 更新任务状态
     * @return
     */
    @Override
    public CloudRecordingTaskLog updateTaskLog(CloudRecordingTaskLog taskLog) {
        taskLog.initUpdate("");
        log.info("更新 录制 VOD 录制任务状态, task={}",taskLog);
        return cloudRecordingTaskLogRepository.save(taskLog);
    }


    /**
     * 开始云端录制
     * @param param
     * @return
     */
    @Override
    public String createCloudRecording(RoomNoParam param){
        String createUser = param.getCreateUser();
        String roomNo = param.getRoomNo();
//        StreamingRoomVO roomInfo = detail(roomNo);
//        if(Objects.isNull(roomInfo) ||
//                !StringUtils.equals(roomInfo.getRecordedStatus(),RoomRecordedEnum.RECORDED.getValue())){
//            log.error("未查询到直播间间信息 或者 直播间未设置录制, roomInfo={}",roomInfo);
//            return "";
//        }
        String userSig = txCloudUtil.getTxCloudUserSig(createUser);
        LiveOpenVo vo = tencentLiveServiceImpl.getSecretByRoomNo(roomNo);

        CloudRecordingParam recordingParam = CloudRecordingParam.builder()
                .roomId(roomNo).userId(createUser).userSig(userSig)
                .sourceContext(vo.getPushUrl()+vo.getSecret()).build();
        TxPullStreamResponse result = txCloudTrtcUtil.createCloudRecording(recordingParam);
        CloudRecordingTaskLog taskLog = CloudRecordingTaskLog.builder()
                .ak(param.getAk()).thirdId(param.getThirdId())
                .roomNo(roomNo).status(VideoRecordTaskStatusEnum.InProgress.getValue())
                .txTaskId(result.getTaskId()).txRequestId(result.getRequestId()).build();
        taskLog.initSave(createUser);
        log.info("保存 开始云端录制 任务信息 taskLog:{}", taskLog);
        cloudRecordingTaskLogRepository.save(taskLog);
        return result.getTaskId();
    }

    /**
     * 创建直播录制
     */
    @Override
    public LiveRecordTaskVO createLiveRecordTask(RoomNoParam param){
        String roomNo = param.getRoomNo();
        LiveRecordTaskVO liveRecordTaskVO = null;
        LiveOpenVo vo = tencentLiveServiceImpl.getSecretByRoomNo(roomNo);

        TxRecordTaskParam recordTaskParam = new TxRecordTaskParam();
        recordTaskParam.setStreamName(roomNo);
        recordTaskParam.setEndTime(DateUtil.getTimeStrampSeconds()/1000+ TxRecordConfig.LIVE_RECORD_MAX_TIME);
        /*if(StringUtils.equals(roomInfo.getType(), RoomTypeEnum.MEETING.getValue())){
            recordTaskParam.setStreamType(1L);//推流类型，默认取值0。  0-直播推流。1-混流
        }*/
        TxPullStreamResponse result = txCloudLiveUtil.createRecordTask(recordTaskParam);
        if(Objects.isNull(result)){
            return liveRecordTaskVO;
        }
        CloudRecordingTaskLog taskLog = CloudRecordingTaskLog.builder().roomNo(roomNo).ak(param.getAk())
                .thirdId(param.getThirdId()).status(VideoRecordTaskStatusEnum.InProgress.getValue())
                .txTaskId(result.getTaskId()).txRequestId(result.getRequestId()).build();
        taskLog.initSave(param.getCreateUser());
        log.info("保存 开始直播录制 任务信息 taskLog:{}", taskLog);
        cloudRecordingTaskLogRepository.save(taskLog);
        liveRecordTaskVO = LiveRecordTaskVO.builder().roomNo(roomNo).taskId(result.getTaskId())
                .taskEndTime(DateUtil.parseYMDHMS2Mils(vo.getPushUrlTimeOut())).build();
        return liveRecordTaskVO;
    }
    /**
     * 停止直播录制任务
     */
    @Override
    public Boolean stopLiveRecordTask(LiveRecordTaskVO liveRecordTask){
        if(Objects.isNull(liveRecordTask)){
            return Boolean.FALSE;
        }
        log.info("停止直播录制任务... liveRecordTask={}",liveRecordTask);
        txCloudLiveUtil.stopRecordTask(liveRecordTask.getTaskId());
        redisUtils.hmDel(RedisKey.LIVE_RECORD_TASK, liveRecordTask.getRoomNo());
        return Boolean.TRUE;
    }


}
