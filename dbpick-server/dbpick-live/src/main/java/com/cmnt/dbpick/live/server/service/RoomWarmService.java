package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.live.api.params.RoomVideoEditParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.vo.RoomWarmInfoVO;

/**
 * 直播间信息配置
 */
public interface RoomWarmService {

    /**
     * 暖场视频信息
     * @param param
     * @return
     */
    RoomWarmInfoVO info(StreamingRoomQueryParams param);

    /**
     * 保存暖场视频信息
     */
    RoomWarmInfoVO saveWarmVideo(RoomVideoEditParam param);

    /**
     * 删除暖场视频信息
     * @param id
     * @param createUser 操作员
     * @return
     */
    Boolean deleteWarmVideo(String id,String createUser);


    /**
     * 编辑暖场视频状态
     * @param param
     * @return
     */
    RoomWarmInfoVO editWarmVideoStatus(RoomVideoEditParam param);





}
