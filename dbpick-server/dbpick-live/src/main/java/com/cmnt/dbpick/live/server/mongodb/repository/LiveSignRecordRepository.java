package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.LiveSignRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播签到记录
 */
public interface LiveSignRecordRepository extends MongoRepository<LiveSignRecord, String> {


}
