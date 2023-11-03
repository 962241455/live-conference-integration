package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.CloudRecordingTaskLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 云端录制任务记录
 */
public interface CloudRecordingTaskLogRepository extends MongoRepository<CloudRecordingTaskLog, String> {

    CloudRecordingTaskLog findTop1ByTxTaskIdOrderByCreateDateTimeDesc(String taskId);

}
