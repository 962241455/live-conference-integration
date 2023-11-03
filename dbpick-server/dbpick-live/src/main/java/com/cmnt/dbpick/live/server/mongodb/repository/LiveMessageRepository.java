package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.LiveMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播消息
 */
public interface LiveMessageRepository extends MongoRepository<LiveMessage, String> {


}
