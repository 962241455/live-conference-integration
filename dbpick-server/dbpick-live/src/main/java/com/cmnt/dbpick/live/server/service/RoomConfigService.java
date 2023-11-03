package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import com.cmnt.dbpick.common.user.RoomConfigVO;
import com.cmnt.dbpick.live.api.model.StreamingRoomInfo;
import com.cmnt.dbpick.live.api.params.RoomConfigEditParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomParams;
import com.cmnt.dbpick.live.api.vo.TxCosConfigVo;

/**
 * 直播房间
 */
public interface RoomConfigService {

    //查询直播间默认配置信息
    RoomConfigVO info(ThirdAccessKeyInfo param);

    /**
     * 编辑直播间默认配置
     */
    RoomConfigVO edit(RoomConfigEditParam param);

    /** 新建房间时处理直播间默认设置 */
    StreamingRoomInfo dealDefaultRoomConfig(StreamingRoomInfo info, StreamingRoomParams param);

    //获取文件最大size (单位 GB)
    Integer getFileMaxSize(String fileType);

    /**
     * 获取cos配置信息
     * @param createBy 用户id
     * @return
     */
    TxCosConfigVo getCosInfo(String createBy);

}
