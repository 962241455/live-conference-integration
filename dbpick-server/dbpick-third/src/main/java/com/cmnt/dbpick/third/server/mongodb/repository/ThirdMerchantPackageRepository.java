package com.cmnt.dbpick.third.server.mongodb.repository;

import com.cmnt.dbpick.third.server.mongodb.document.ThirdMerchantPackage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThirdMerchantPackageRepository extends MongoRepository<ThirdMerchantPackage, String> {

    ThirdMerchantPackage findByNameAndDeleted(String name, Boolean deleted);
}
