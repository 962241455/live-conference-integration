package com.cmnt.dbpick.live.server.mq.consumer;


import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum;
import com.cmnt.dbpick.common.mq.constant.RocketMQConstant;

import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.live.server.mongodb.document.LiveVideos;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveVideosRepository;
import com.cmnt.dbpick.live.server.service.RoomPlaybackService;
import com.cmnt.dbpick.live.server.service.impl.VideoTranscodingRecordServiceImpl;
import com.cmnt.dbpick.transcoding.api.message.VideoVodResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * 转码完成回调
 */
@Component
@RocketMQMessageListener(
        topic = RocketMQConstant.DELAY_TOPIC_VIDEO_TRANSCODING_END,
        consumerGroup = RocketMQConstant.DELAY_TOPIC_VIDEO_TRANSCODING_END)
@Slf4j
public class VideoTranscodeResponseConsumer implements RocketMQListener<String> {

    @Autowired
    private LiveVideosRepository liveVideosRepository;
    @Autowired
    private VideoTranscodingRecordServiceImpl videoTranscodingRecordServiceImpl;
    @Autowired
    private RoomPlaybackService roomPlaybackServiceImpl;

    @Override
    public void onMessage(String message) {
        log.info("VideoTranscodeResponseConsumer>>>视频转码完成回调: message={}",message);
        if(StringUtils.isBlank(message)){
            return;
        }
        VideoVodResponse response = JSON.parseObject(message, VideoVodResponse.class);
        log.info("视频转码完成回调:相应参数 response={}",response);
        if(Objects.isNull(response) || StringUtils.isBlank(response.getVideoId())){
            log.error("视频转码完成回调:相应参数/视频id为空，response={}",response);
            return;
        }
        String videoId = response.getVideoId();
        Optional<LiveVideos> byId = liveVideosRepository.findById(videoId);
        if(!byId.isPresent()){
            log.error("视频转码完成回调:{}，videoId={}", ResponseCode.NOT_FIND_VIDEO.getMsg(),videoId);
            return;
        }
        LiveVideos video = byId.get();
        if(StringUtils.equals(ResponseCode.HTTP_RES_CODE_200.getCode(),response.getCode())){
            video.setCover(response.getCover());
            video.setWidth(response.getWidth());
            video.setHeight(response.getHeight());
            if(Objects.isNull(video.getSeconds())){
                video.setSeconds(new Double(response.getDuration()).intValue());
                video.setDuration(DateUtil.formatMilliSecondToTimeStr(new Double(response.getDuration()).longValue()*1000));
            }
            if(Objects.nonNull(response.getSize())){
                video.setFileSize(response.getSize());
            }
            video.setVideoURL(response.getMediaStoragePath());
            video.setTranscodeStatus(VideoTranscodeStatusEnum.FINISH.getValue());
            video.setTxRequestId(response.getRequestId());
            log.info("VideoTranscodeResponseConsumer>>>视频转码完成回调:转码成功,保存视频转码记录信息 video={}",video);
            roomPlaybackServiceImpl.updatePlaybackCoverWithFileId(video.getFileId(), response.getCover());
        }else{
            if(!StringUtils.equals(VideoTranscodeStatusEnum.FINISH.getValue(),video.getTranscodeStatus())){
                video.setTranscodeStatus(VideoTranscodeStatusEnum.FAILED.getValue());
            }
            log.error("VideoTranscodeResponseConsumer>>>视频转码完成回调:转码失败,保存视频转码记录信息 video={}",video);
        }
        liveVideosRepository.save(video);
        // 保存视频转码记录
        videoTranscodingRecordServiceImpl.saveVideoTranscodeRecord(video);
    }

}
