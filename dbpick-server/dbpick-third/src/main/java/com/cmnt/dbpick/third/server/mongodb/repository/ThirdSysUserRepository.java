package com.cmnt.dbpick.third.server.mongodb.repository;

import com.cmnt.dbpick.third.server.mongodb.document.ThirdSysUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 第三方商户账号
 */
public interface ThirdSysUserRepository extends MongoRepository<ThirdSysUser, String> {

    /**
     * 查询第三方商户账号是否已存在
     * @param thirdId 第三方商户账号
     * @param deleted
     * @return
     */
    ThirdSysUser findTop1ByThirdIdAndDeleted(String thirdId, boolean deleted, Sort sort);

    ThirdSysUser findBySaltAndAndDeleted(String salt, Boolean deleted);
}
