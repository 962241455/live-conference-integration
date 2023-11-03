package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.WatchRegisterInfoVO;

/**
 * 直播观看限制
 */
public interface WatchFilterService {

    /**
     * 取消直播观看限制
     * @param param
     * @return
     */
    Boolean cancelFilter(RoomWatchFilterParam param);
    /**
     * 关闭游客观看
     * @param param
     * @return
     */
    Boolean closeVisitorWatch(RoomWatchFilterParam param);

    /**
     * 保存直播观看限制
     * @param param
     * @return
     */
    Boolean saveFilter(RoomWatchFilterRegisterParam param);


    /**
     * 查询直播观看限制
     * @param param
     * @return
     */
    WatchRegisterInfoVO findFilter(RoomWatchFilterParam param);
}
