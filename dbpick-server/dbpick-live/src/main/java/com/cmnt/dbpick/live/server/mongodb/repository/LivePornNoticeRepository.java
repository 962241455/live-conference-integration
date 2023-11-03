package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.LivePornNotice;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 直播鉴黄通知
 */
public interface LivePornNoticeRepository extends MongoRepository<LivePornNotice, String> {

}
