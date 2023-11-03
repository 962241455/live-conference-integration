package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.LiveVideos;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 录播视频库
 */
public interface LiveVideosRepository extends MongoRepository<LiveVideos, String> {

    LiveVideos findTop1ByFileIdOrderByCreateDateTimeDesc(String fileId);

}
