package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.utils.AccessAuthUtil;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.live.server.mongodb.document.LiveVideos;
import com.cmnt.dbpick.live.server.mongodb.document.VideoTranscodingRecord;
import com.cmnt.dbpick.live.server.mongodb.repository.VideoTranscodingRecordRepository;
import com.cmnt.dbpick.live.server.service.VideoTranscodingRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 视频转码记录服务
 */

@Slf4j
@Service
public class VideoTranscodingRecordServiceImpl implements VideoTranscodingRecordService {

    @Autowired
    private AccessAuthUtil accessAuthUtil;

    @Autowired
    private VideoTranscodingRecordRepository videoTranscodingRecordRepository;

    /**
     * 保存视频转码记录
     */
    @Async
    @Override
    public void saveVideoTranscodeRecord(LiveVideos video) {
        log.info("异步保存视频转码记录,video={}",video);
        String thirdAK = accessAuthUtil.getThirdAK(video.getThirdId());
        VideoTranscodingRecord record = new VideoTranscodingRecord();
        FastBeanUtils.copy(video,record);
        record.setTransDate(DateUtil.nowDateTime(DateUtil.Y_M_D));
        record.setTransDuration(DateUtil.getTimeStrampSeconds()-video.getCreateDateTime());
        record.setTransStatus(video.getTranscodeStatus());
        record.setAk(thirdAK);
        record.initSave(video.getCreateUser());
        videoTranscodingRecordRepository.save(record);
    }


    /**
     * 根据fileId更新转码视频路径
     */
    @Async
    @Override
    public void transcodeVodVideo(String fileId,String transVideoUrl, String videoTransStatus){
        log.info("根据fileId更新转码视频路径 fileId={}, transVideoUrl={}, videoTransStatus={}",
                fileId,transVideoUrl,videoTransStatus);
        if(StringUtils.isBlank(fileId)){
            throw new BizException(ResponseCode.NULL_VIDEO_URL);
        }
        VideoTranscodingRecord record = videoTranscodingRecordRepository.findTop1ByFileIdOrderByCreateDateTimeDesc(fileId);
        if(Objects.isNull(record)){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        record.setVideoURL(transVideoUrl);
        record.setTransStatus(videoTransStatus);
        record.initUpdate("");
        videoTranscodingRecordRepository.save(record);
    }

    /**
     * 更新转码记录-转码视频被使用情况
     * @param fileId
     * @param roomNo
     */
    @Async
    @Override
    public void updateTransRecordRoomByFileId(String fileId, String roomNo) {
        log.info("更新转码记录-转码视频被使用情况 fileId={}, 被用于直播间 roomNo={}", fileId,roomNo);
        VideoTranscodingRecord record = videoTranscodingRecordRepository
                .findTop1ByFileIdAndTransStatusOrderByCreateDateTimeDesc(
                        fileId, VideoTranscodeStatusEnum.FINISH.getValue());
        if(Objects.isNull(record)){
            return;
        }
        Set<String> usedRoomNos = record.getUsedRoomNos();
        log.info("更新转码记录-转码视频被使用情况 原记录信息 record={}", record);
        if(Objects.isNull(usedRoomNos)){
            usedRoomNos = new HashSet<String>();
        }
        usedRoomNos.add(roomNo);
        record.setUsedRoomNos(usedRoomNos);
        log.info("更新转码记录-转码视频被使用情况 更新记录信息 record={}", record);
        videoTranscodingRecordRepository.save(record);
    }

}
