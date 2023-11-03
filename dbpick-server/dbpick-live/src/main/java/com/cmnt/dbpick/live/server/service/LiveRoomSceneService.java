package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.LiveRoomSceneParam;
import com.cmnt.dbpick.live.api.params.LiveRoomSceneSwitchParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.vo.LiveRoomSceneVO;

import java.util.List;


/**
 * 直播间菜单设置相关接口
 */
public interface LiveRoomSceneService {

    /**
     * 添加直播现场
     */
    LiveRoomSceneVO addScene(LiveRoomSceneParam param);

    /**
     * 直播现场数据分页列表(管理端)
     */
    PageResponse<LiveRoomSceneVO> getScenePageList(StreamingRoomQueryParams param);

    /**
     * 更新上下架状态
     */
    LiveRoomSceneVO updateSceneStatus(LiveRoomSceneSwitchParam param);


    /**
     * 可用的直播现场列表
     */
    List<LiveRoomSceneVO> usefulList(RoomNoParam param);
    /**
     * 发送直播现场im消息
     */
    void asyncSendImSceneMsg(RoomNoParam param);
}
