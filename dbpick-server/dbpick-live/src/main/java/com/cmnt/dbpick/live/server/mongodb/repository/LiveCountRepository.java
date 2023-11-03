package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.LiveCount;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播记录流水
 * @author
 * @date 2022/7/25
 */
public interface LiveCountRepository  extends MongoRepository<LiveCount, String> {

    LiveCount findTopByRoomNoOrderByCreateDateTimeDesc(String roomNo);

}
