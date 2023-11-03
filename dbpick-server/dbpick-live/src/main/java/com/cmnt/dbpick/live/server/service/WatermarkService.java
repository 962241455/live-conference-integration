package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.LiveWatermarkVO;

/**
 * 水印管理
 */
public interface WatermarkService {

    /**
     * 查询水印图片列表
     */
    PageResponse<LiveWatermarkVO> getList(LiveWatermarkQueryParams param);

    /**
     * 添加水印
     * @param param
     * @return
     */
    Boolean add(LiveWatermarkEditParam param);

    /**
     * 更新水印图片
     * @param param
     * @return
     */
    LiveWatermarkVO update(LiveWatermarkEditParam param);

    /**
     * 删除水印
     * @param id 水印id
     * @param createUser 操作员
     * @return
     */
    Boolean deleteWatermark(String id, String createUser);

    /**
     * 添加直播间水印
     * @param param
     * @return
     */
    Boolean createLive(LiveRoomWatermarkParam param);

    /**
     * 取消直播间水印
     * @param param
     * @return
     */
    Boolean cancelLive(LiveRoomWatermarkParam param);


    /**
     * 取消所有使用改水印的直播间
     * @param id
     * @param createUser 操作员
     * @return
     */
    Boolean cancelWatermarkAll(String id, String createUser);
}
