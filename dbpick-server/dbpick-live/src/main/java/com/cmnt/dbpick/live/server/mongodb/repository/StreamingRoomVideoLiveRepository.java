package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomVideoLive;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 视频直播
 */
public interface StreamingRoomVideoLiveRepository extends MongoRepository<StreamingRoomVideoLive, String> {

    StreamingRoomVideoLive findTop1ByTxTaskIdOrderByCreateDateTimeDesc(String taskId);

}
