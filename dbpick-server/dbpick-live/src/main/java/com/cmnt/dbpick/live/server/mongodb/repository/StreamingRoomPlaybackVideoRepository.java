package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomPlaybackVideo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 直播间回放视频
 */
public interface StreamingRoomPlaybackVideoRepository extends MongoRepository<StreamingRoomPlaybackVideo, String> {

    StreamingRoomPlaybackVideo findTop1ByRoomNoOrderByPlaySortDesc(String roomNo);

    StreamingRoomPlaybackVideo findTop1ByRoomNoAndDeletedAndPlaySortLessThanOrderByPlaySortDesc(String roomNo,Boolean deleted,Integer playSort);
    StreamingRoomPlaybackVideo findTop1ByRoomNoAndDeletedAndPlaySortGreaterThanOrderByPlaySortAsc(String roomNo,Boolean deleted,Integer playSort);

    List<StreamingRoomPlaybackVideo> findTop10ByRoomNoAndDeletedAndPlaySortGreaterThanOrderByPlaySortAsc(String roomNo,Boolean deleted,Integer playSort);

}
