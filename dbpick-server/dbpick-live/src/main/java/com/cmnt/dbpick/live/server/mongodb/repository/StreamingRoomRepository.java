package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播间
 */
public interface StreamingRoomRepository extends MongoRepository<StreamingRoom, String> {

    StreamingRoom findTop1ByRoomNoOrderByCreateDateDesc(String roomNo);

}
