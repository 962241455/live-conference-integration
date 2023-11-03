package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.live.api.message.RoomPlayRecordMessage;
import com.cmnt.dbpick.live.api.params.RoomPlayRecordParams;

/**
 * 房间播放记录
 */
public interface RoomPlayRecordService {

    /**
     * 保存直播开始记录
     */
    Boolean savePlayStartRecord(RoomPlayRecordParams params);

    /**
     * 更新直播结束记录
     */
    RoomPlayRecordMessage updatePlayEndRecord(RoomPlayRecordParams params);


}
