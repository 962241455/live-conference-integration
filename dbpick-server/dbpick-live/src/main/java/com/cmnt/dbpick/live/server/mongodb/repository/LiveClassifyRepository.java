package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.server.mongodb.document.LiveClassify;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LiveClassifyRepository  extends MongoRepository<LiveClassify, String> {


}