package com.cmnt.dbpick.stats.server.es.repository;

import com.cmnt.dbpick.stats.server.es.document.RoomUserRecordIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoomUserRecordEsRepository extends ElasticsearchRepository<RoomUserRecordIndex,String> {


}
