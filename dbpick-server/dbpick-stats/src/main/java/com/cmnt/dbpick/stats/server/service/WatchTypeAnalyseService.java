package com.cmnt.dbpick.stats.server.service;

import com.cmnt.dbpick.stats.api.vo.es.WatchTimesPageParam;

/**
 * 根据观看类型分析数据业务
 */
public interface WatchTypeAnalyseService {

    void executeAnalyse(WatchTimesPageParam analysePageParam, String roomNo);

}
