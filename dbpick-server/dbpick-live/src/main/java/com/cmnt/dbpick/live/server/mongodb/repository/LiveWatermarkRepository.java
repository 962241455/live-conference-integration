package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.LiveWatermark;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播水印
 */
public interface LiveWatermarkRepository extends MongoRepository<LiveWatermark, String> {

    LiveWatermark findTop1ByThirdIdAndWatermarkIdOrderByCreateDateTimeDesc(String thirdId,Long watermarkId);

    LiveWatermark findTop1ByWatermarkIdOrderByCreateDateTimeDesc(Long watermarkId);

}
