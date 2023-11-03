package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomWarm;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播间视频
 */
public interface StreamingRoomWarmRepository extends MongoRepository<StreamingRoomWarm, String> {

    StreamingRoomWarm findTop1ByRoomNoAndDeletedOrderByCreateDateDesc(String roomNo, Boolean deleted);

}
