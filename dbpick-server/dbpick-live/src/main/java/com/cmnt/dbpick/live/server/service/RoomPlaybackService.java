package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.RoomPlaybackInfoVO;
import com.cmnt.dbpick.live.api.vo.RoomPlaybackVO;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomPlaybackVideo;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomVideoLive;

import java.util.List;

/**
 * 直播房间回放配置
 */
public interface RoomPlaybackService {

    /**
     * 房间回放列表
     */
    PageResponse<RoomPlaybackVO> list(StreamingRoomQueryParams param);

    /**
     * 编辑回放配置
     */
    Boolean editPlayback(RoomPlaybackParam param);

    /**
     * 保存直播录制视频到回放列表
     */
    void saveRecordVideo(StreamingRoomVideoLive param);
    void saveRecordVideo(RoomVideoEditParam param);
    StreamingRoomPlaybackVideo addRoomPlaybackVideo(RoomVideoEditParam param);

    /**
     * 商户端添加回放视频
     * @param videosParam
     * @return
     */
    Boolean pushVideos(RoomPlaybackVideosParam videosParam);

    /**
     * 编辑回放视频名称
     */
    RoomPlaybackVO editPlayName(RoomPlaybackNameParam param);
    /**
     * 编辑回放视频顺序
     */
    Boolean editPlaySort(RoomPlaybackSortParam param);

    /**
     * 删除回放视频视频
     */
    Boolean deletePlaybackVideo(String id, String userId);

    /**
     * 查询房间回放信息
     */
    RoomPlaybackInfoVO listTop10(RoomPlaybackQueryParam params);


    /**
     * 根据文件id跟新回放视频封面;
     * @param fileId
     * @param cover
     */
    void updatePlaybackCoverWithFileId(String fileId, String cover);


}
