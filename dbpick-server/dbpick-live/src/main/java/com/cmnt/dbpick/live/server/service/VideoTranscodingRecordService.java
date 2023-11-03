package com.cmnt.dbpick.live.server.service;
import com.cmnt.dbpick.live.server.mongodb.document.LiveVideos;

/**
 * 视频转码记录服务
 */
public interface VideoTranscodingRecordService {

    /**
     * 保存视频转码记录
     */
    void saveVideoTranscodeRecord(LiveVideos video);

    /**
     * 根据fileId更新(TX VOD)转码视频路径
     */
    void transcodeVodVideo(String fileId,String transVideoUrl, String videoTransStatus);

    /**
     * 更新转码记录-转码视频被使用情况
     * @param fileId
     * @param roomNo
     */
    void updateTransRecordRoomByFileId(String fileId, String roomNo);

}
