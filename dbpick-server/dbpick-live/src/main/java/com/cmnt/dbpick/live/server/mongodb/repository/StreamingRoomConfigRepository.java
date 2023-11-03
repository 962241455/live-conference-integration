package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播间默认配置
 */
public interface StreamingRoomConfigRepository extends MongoRepository<StreamingRoomConfig, String> {

    StreamingRoomConfig findTop1ByThirdIdOrderByCreateDateTimeDesc(String thirdId);

}
