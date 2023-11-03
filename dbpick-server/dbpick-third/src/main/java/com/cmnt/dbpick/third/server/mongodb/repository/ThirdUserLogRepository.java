package com.cmnt.dbpick.third.server.mongodb.repository;

import com.cmnt.dbpick.third.server.mongodb.document.ThirdUserLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 第三方商户账号登录日志
 */
public interface ThirdUserLogRepository extends MongoRepository<ThirdUserLog, String> {


}
