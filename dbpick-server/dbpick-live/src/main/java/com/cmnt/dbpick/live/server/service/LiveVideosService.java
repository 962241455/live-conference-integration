package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.LiveVideosQueryParams;
import com.cmnt.dbpick.live.api.params.RoomVideoEditParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.params.VideoPushRoomParam;
import com.cmnt.dbpick.live.api.vo.RoomVideoLivesVO;
import com.cmnt.dbpick.live.api.vo.RoomVideoVO;
import com.cmnt.dbpick.live.server.mongodb.document.LiveVideos;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomVideoLive;

/**
 * 直播间信息配置
 */
public interface LiveVideosService {

    /**
     * 查询查询视频库列表
     */
    PageResponse<RoomVideoVO> getVideoList(LiveVideosQueryParams param);
    /**
     * 获取云点播视频上传签名
     * @return
     */
    String getVodUploadSign();

    //获取文件最大size (单位 GB)
    Integer getFileMaxSize();

    /**
     * 保存上传视频库成功的视频信息
     */
    Boolean saveVodVideo(RoomVideoEditParam param);
    /**
     * TRTC录制完成 视频路径-保存
     */
    LiveVideos saveRecordVideo(RoomVideoEditParam param, Boolean needTranscode);
    /**
     * 删除视频库视频
     * @param id
     * @param createUser 操作员
     * @return
     */
    Boolean deleteVideo(String id, String createUser);
    /**
     * 删除未保存视频库视频
     * @param fileId
     * @return
     */
    Boolean deleteVideoByFileId(String fileId, String operate);
    /**
     * 对视频库视频进行转码
     * @param param
     * @return
     */
    RoomVideoVO transcodeVideo(RoomVideoEditParam param);


    /**
     * 视频录播任务列表
     * @param param
     * @return
     */
    PageResponse<RoomVideoLivesVO> videoTaskLive(StreamingRoomQueryParams param);
    /**
     * 添加/编辑视频录播任务
     * @param param
     * @return
     */
    String editVideoTask(VideoPushRoomParam param);
    /**
     * 删除视频录播任务
     * @param id 视频任务id
     * @param createUser 操作员
     * @return
     */
    Boolean deleteVideoTask(String id, String createUser);




    /**
     * 删除房间之后删除对录播任务
     * @param roomNo 房间号
     * @return
     */
    Boolean deleteVideoLiveByRoomNo(String roomNo);


    /**
     * 根据任务id查询直播视频信息
     * @param taskId
     * @return
     */
    StreamingRoomVideoLive findVideoByTaskId(String taskId);

    /**
     * 根据任务id 更新 直播视频信息状态
     * @param taskId
     * @param status  TaskStart-任务开始
     *                VodSourceFileStart-点播文件开始
     *                VodSourceFileFinish-点播文件结束
     *                TaskExit - 任务退出
     * @return
     */
    Boolean updateStatus(String taskId, String status);

    /**
     * 根据房间状态（启用/封禁）处理视频
     * @param roomNo
     * @param roomStatus forbidden-房间封禁
     */
    Boolean handleVideoByRoomStatus(String roomNo, String roomStatus);

    /**
     * 查询录播房间所有（已到过期时间但是状态不是过期的）视频任务
     */
    Boolean handleVideoStatusByRoomNo(String roomNo);

}
