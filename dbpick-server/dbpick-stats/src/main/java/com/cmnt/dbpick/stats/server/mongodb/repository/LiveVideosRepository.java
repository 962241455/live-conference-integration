package com.cmnt.dbpick.stats.server.mongodb.repository;

import com.cmnt.dbpick.stats.server.mongodb.document.LiveVideos;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 视频库
 */
public interface LiveVideosRepository extends MongoRepository<LiveVideos, String> {

    List<LiveVideos> findByThirdIdAndCreateDateTimeBetweenOrderByCreateDateTimeDesc(
            String thirdId, Long startTimestamp,Long endTimestamp);

    LiveVideos findTop1ByFileIdOrderByCreateDateTimeDesc(String fileId);

}
