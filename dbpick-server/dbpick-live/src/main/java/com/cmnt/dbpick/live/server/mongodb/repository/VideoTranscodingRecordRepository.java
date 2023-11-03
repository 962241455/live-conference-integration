package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.VideoTranscodingRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 视频转码记录
 */
public interface VideoTranscodingRecordRepository extends MongoRepository<VideoTranscodingRecord, String> {

    VideoTranscodingRecord findTop1ByFileIdOrderByCreateDateTimeDesc(String fileId);

    VideoTranscodingRecord findTop1ByFileIdAndTransStatusOrderByCreateDateTimeDesc(String fileId, String transStatus);


}
