package com.cmnt.dbpick.stats.server.mongodb.repository;

import com.cmnt.dbpick.stats.server.mongodb.document.StreamingRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播间
 */
public interface StreamingRoomRepository extends MongoRepository<StreamingRoom, String> {

    StreamingRoom findTop1ByRoomNoOrderByCreateDateDesc(String roomNo);

}
