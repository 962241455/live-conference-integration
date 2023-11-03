package com.cmnt.dbpick.third.server.mongodb.repository;

import com.cmnt.dbpick.third.server.mongodb.document.ThirdUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 商户子账号
 */
public interface ThirdUserRepository extends MongoRepository<ThirdUser, String> {

    /**
     * 查询 商户子账号 是否已存在
     * @param thirdId 第三方商户账号
     * @param deleted
     * @return
     */
    ThirdUser findTop1ByThirdIdAndUserIdAndDeleted(String thirdId, String userId, boolean deleted, Sort sort);


    ThirdUser findTop1ByUserIdAndDeleted(String userId, boolean deleted, Sort sort);


}
