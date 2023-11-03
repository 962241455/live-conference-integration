package com.cmnt.dbpick.stats.server.es.repository;

import com.cmnt.dbpick.stats.server.es.document.RoomTrtcUsageIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoomTrtcUsageEsRepository extends ElasticsearchRepository<RoomTrtcUsageIndex,String> {


}
