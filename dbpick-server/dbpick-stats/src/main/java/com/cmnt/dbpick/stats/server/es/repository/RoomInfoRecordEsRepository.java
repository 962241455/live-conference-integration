package com.cmnt.dbpick.stats.server.es.repository;

import com.cmnt.dbpick.stats.server.es.document.RoomInfoRecordIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoomInfoRecordEsRepository extends ElasticsearchRepository<RoomInfoRecordIndex,String> {


}
