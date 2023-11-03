package com.cmnt.dbpick.third.server.mongodb.repository;

import com.cmnt.dbpick.common.user.ThirdAccessKeyVO;
import com.cmnt.dbpick.third.server.mongodb.document.ThirdAccessKey;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * 平台访问凭证
 */
public interface ThirdAccessKeyRepository extends MongoRepository<ThirdAccessKey, String> {

    ThirdAccessKeyVO findTop1ByIdAndDeleted(String id, Boolean deleted, Sort sort);
    ThirdAccessKeyVO findTop1ByThirdIdAndDeleted(String thirdId, Boolean deleted, Sort sort);

    ThirdAccessKeyVO findTop1ByThirdIdAndStatusAndDeleted(String thirdId, String status, Boolean deleted, Sort sort);

    ThirdAccessKeyVO findTop1ByAccessKeyIdAndAccessKeySecretAndStatusAndDeletedOrderByCreateDateDesc(String ak, String sk, String status, Boolean deleted);

}
