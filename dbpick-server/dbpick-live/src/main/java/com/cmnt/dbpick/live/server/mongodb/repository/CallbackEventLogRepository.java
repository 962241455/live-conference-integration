package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.CallbackEventLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CallbackEventLogRepository extends MongoRepository<CallbackEventLog, String> {

}
