package com.cmnt.dbpick.live.server.service;


import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.live.api.vo.redis.LiveRecordTaskVO;
import com.cmnt.dbpick.live.server.mongodb.document.CloudRecordingTaskLog;

/**
 * 云端录制任务记录
 */
public interface LiveRecordTaskLogService {

    /**
     * 根据任务id查询记录
     * @return
     */
    CloudRecordingTaskLog findLogByTaskId(String taskId);

    /**
     * 更新任务状态
     * @return
     */
    CloudRecordingTaskLog updateTaskLog(CloudRecordingTaskLog taskLog);

    /**
     * 开始云端录制
     * @param param
     * @return
     */
    String createCloudRecording(RoomNoParam param);


    /**
     * 创建直播录制任务
     * @param param
     * @return
     */
    LiveRecordTaskVO createLiveRecordTask(RoomNoParam param);
    /**
     * 停止直播录制任务
     */
    Boolean stopLiveRecordTask(LiveRecordTaskVO liveRecordTaskVO);

}
