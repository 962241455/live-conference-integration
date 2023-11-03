package com.cmnt.dbpick.manager.server.mongodb.repository;

import com.cmnt.dbpick.manager.server.mongodb.document.SysManager;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 *
 */
public interface SysManagerRepository extends MongoRepository<SysManager, String> {

    SysManager findTop1ByUserIdAndDeleted(String userId, Boolean deleted, Sort sort);

    SysManager findTop1ByUserId(String userId);


}
