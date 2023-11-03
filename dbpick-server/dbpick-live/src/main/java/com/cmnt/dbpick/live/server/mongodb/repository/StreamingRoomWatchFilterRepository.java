package com.cmnt.dbpick.live.server.mongodb.repository;


import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomWatchFilter;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播间观看限制
 */
public interface StreamingRoomWatchFilterRepository extends MongoRepository<StreamingRoomWatchFilter, String> {

    StreamingRoomWatchFilter findTop1ByRoomNoAndFilterTypeAndDeletedOrderByCreateDateDesc(
            String roomNo, String filterType, Boolean deleted);

}
