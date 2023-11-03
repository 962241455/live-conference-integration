package com.cmnt.dbpick.live.server.tencent.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.tx.tencent.response.vod.VodProdStateChangedResponse;
import com.cmnt.dbpick.common.tx.tencent.response.vod.eventInfo.MediaProcessResultSetEvent;
import com.cmnt.dbpick.live.server.mongodb.document.LiveVideos;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveVideosRepository;
import com.cmnt.dbpick.live.server.service.VideoTranscodingRecordService;
import com.cmnt.dbpick.live.server.tencent.enums.VodCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.event.VodHandlerEventType;
import com.cmnt.dbpick.live.server.tencent.service.VodCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 转码任务流状态变更
 */
@Slf4j
@Component
@VodHandlerEventType(VodCallBackEnum.PROCEDURE_STATE_CHANGED)
public class VodProcedureCallBackServiceImpl implements VodCallBackService {

    @Autowired
    private LiveVideosRepository liveVideosRepository;
    @Autowired
    private VideoTranscodingRecordService videoTranscodingRecordService;

    @Override
    public void execute(String jsonObjectStr) {
        log.info("云点播视频处理 转码任务流状态变更 返回参数 str={}", jsonObjectStr);
        VodProdStateChangedResponse res = JSON.parseObject(jsonObjectStr, VodProdStateChangedResponse.class);
        log.info("云点播视频处理 转码任务流状态变更 返回参数 response={}", res);
        if(Objects.isNull(res.getProcedureStateChangeEvent())
            || !StringUtils.equals(res.getProcedureStateChangeEvent().getStatus(), VideoTranscodeStatusEnum.FINISH.getValue())){
            log.error("转码尚在处理中 ....");
            return;
        }
        String fileId = res.getProcedureStateChangeEvent().getFileId();
        LiveVideos videos = liveVideosRepository.findTop1ByFileIdOrderByCreateDateTimeDesc(fileId);
        if(Objects.isNull(videos)){
            log.error(ResponseCode.NOT_FIND_VIDEO.getMsg());
            return;
        }
        log.info("更新视频信息....");
        MediaProcessResultSetEvent media = res.getProcedureStateChangeEvent().getMediaProcessResultSet()[0];
        String url = media.getTranscodeTask().getOutput().getUrl();
        videos.setTranscodeStatus(VideoTranscodeStatusEnum.FINISH.getValue());
        videos.setVideoURL(url);
        videos.initUpdate("");
        liveVideosRepository.save(videos);
        videoTranscodingRecordService.transcodeVodVideo(videos.getFileId(),videos.getVideoURL(),videos.getTranscodeStatus());
    }

}
