package com.cmnt.dbpick.stats.server.es.repository;

import com.cmnt.dbpick.stats.server.es.document.LiveTrtcUsageIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LiveTrtcUsageEsRepository extends ElasticsearchRepository<LiveTrtcUsageIndex,String> {


}
