package com.cmnt.dbpick.stats.server.mongodb.repository;

import com.cmnt.dbpick.stats.server.mongodb.document.LiveVote;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播投票
 */
public interface LiveVoteRepository extends MongoRepository<LiveVote, String> {

}
