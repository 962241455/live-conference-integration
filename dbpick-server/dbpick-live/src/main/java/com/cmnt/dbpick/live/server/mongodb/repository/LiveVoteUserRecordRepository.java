package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.LiveVoteUserRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播用户投票记录
 */
public interface LiveVoteUserRecordRepository extends MongoRepository<LiveVoteUserRecord, String> {


}
