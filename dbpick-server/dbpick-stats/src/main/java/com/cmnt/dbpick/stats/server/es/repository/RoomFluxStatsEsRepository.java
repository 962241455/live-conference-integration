package com.cmnt.dbpick.stats.server.es.repository;

import com.cmnt.dbpick.stats.server.es.document.RoomFluxStatsIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoomFluxStatsEsRepository extends ElasticsearchRepository<RoomFluxStatsIndex,String> {


}
