package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomPlayRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播间播放记录
 */
public interface StreamingRoomPlayRecordRepository extends MongoRepository<StreamingRoomPlayRecord, String> {

    StreamingRoomPlayRecord findTop1ByRoomNoAndStatusOrderByCreateDateDesc(String roomNo,String status);

}
