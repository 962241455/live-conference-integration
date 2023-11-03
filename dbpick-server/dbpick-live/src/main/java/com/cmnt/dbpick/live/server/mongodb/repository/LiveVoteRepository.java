package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.LiveVote;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播投票
 */
public interface LiveVoteRepository extends MongoRepository<LiveVote, String> {

    LiveVote findTop1ByRoomNoAndStatusOrderByCreateDateTimeDesc(String roomNo, String status);

}
