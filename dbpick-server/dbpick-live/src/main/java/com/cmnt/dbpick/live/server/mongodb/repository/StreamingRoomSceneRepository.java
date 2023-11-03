package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomScene;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播现场
 */
public interface StreamingRoomSceneRepository extends MongoRepository<StreamingRoomScene, String> {

}
