package com.cmnt.dbpick.stats.server.mq.service;

import com.cmnt.dbpick.live.api.message.RoomPlayRecordMessage;

/**
 * 根据直播记录统计直播数据
 */
public interface RoomPlayRecordStatsService {

    void execute(RoomPlayRecordMessage record);

}
