package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.LiveRoomMenuVO;

import java.util.List;

/**
 * 直播间菜单设置相关接口
 */
public interface LiveRoomMenuService {

    /**
     * 查询菜单设置列表
     * @param param
     * @return
     */
    List<LiveRoomMenuVO> list(RoomNoParam param);


    /**
     * 添加/编辑菜单设置
     * @param param
     * @return
     */
    LiveRoomMenuVO edit(LiveRoomMenuParam param);

    /**
     * 删除菜单设置
     * @param id
     * @param userId
     */
    Boolean deleteMenu(String id, String userId);

}
