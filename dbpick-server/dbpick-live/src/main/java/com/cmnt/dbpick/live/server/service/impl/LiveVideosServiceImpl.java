package com.cmnt.dbpick.live.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.enums.PullStreamStatusEnum;
import com.cmnt.dbpick.common.enums.live.RoomStatusEnum;
import com.cmnt.dbpick.common.enums.live.VideoSourceTypeEnum;
import com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum;
import com.cmnt.dbpick.common.enums.tencent.TxVideoSourceType;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.mq.RocketMQProducer;
import com.cmnt.dbpick.common.mq.constant.RocketMQConstant;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.tx.tencent.TxCloudLiveUtil;
import com.cmnt.dbpick.common.tx.tencent.TxCloudVodUtil;
import com.cmnt.dbpick.common.tx.tencent.request.live.TxPushStreamModifyParam;
import com.cmnt.dbpick.common.tx.tencent.request.live.TxPushStreamParam;
import com.cmnt.dbpick.common.tx.tencent.response.TxPullStreamResponse;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.live.api.params.LiveVideosQueryParams;
import com.cmnt.dbpick.live.api.params.RoomVideoEditParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.params.VideoPushRoomParam;
import com.cmnt.dbpick.live.api.vo.LiveOpenVo;
import com.cmnt.dbpick.live.api.vo.RoomVideoLivesVO;
import com.cmnt.dbpick.live.api.vo.RoomVideoVO;
import com.cmnt.dbpick.live.api.vo.redis.LiveStreamStopVO;
import com.cmnt.dbpick.live.server.mongodb.document.LiveVideos;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomVideoLive;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomVideoLiveRepository;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveVideosRepository;
import com.cmnt.dbpick.live.server.service.*;
import com.cmnt.dbpick.transcoding.api.message.VideoVodMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 直播间信息配置
 */
@Slf4j
@Service
public class LiveVideosServiceImpl implements LiveVideosService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TxCloudVodUtil txCloudVodUtil;
    @Autowired
    private TxCloudLiveUtil txCloudLiveUtil;
    @Autowired
    private TxCloudUtil txCloudUtil;
    @Autowired
    private AccessAuthUtil accessAuthUtil;

    @Autowired
    private MongoPageHelper mongoPageHelper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RocketMQProducer rocketMQProducer;

    @Autowired
    private LiveVideosRepository liveVideosRepository;
    @Autowired
    private StreamingRoomVideoLiveRepository streamingRoomVideoLiveRepository;

    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;
    @Autowired
    private RoomPlaybackService roomPlaybackServiceImpl;
    @Autowired
    private TencentLiveService tencentLiveService;
    @Autowired
    private VideoTranscodingRecordService videoTranscodingRecordServiceImpl;


    /**
     * 查询视频库列表
     */
    @Override
    public PageResponse<RoomVideoVO> getVideoList(LiveVideosQueryParams param){
        Query query = new Query();
        if (StringUtils.isNotBlank(param.getFileName())) {
            query.addCriteria(Criteria.where("fileName").regex(param.getFileName()));
        }
        if (StringUtils.isNotBlank(param.getFileId())) {
            query.addCriteria(Criteria.where("fileId").regex(param.getFileId()));
        }
        if (Objects.nonNull(param.getSearchStartTime()) && Objects.nonNull(param.getSearchEndTime())) {
            query.addCriteria(Criteria.where("createDateTime")
                    .gte(param.getSearchStartTime()).lte(param.getSearchEndTime()));
        }
        if (StringUtils.isNotBlank(param.getTranscodeStatus())) {
            if(StringUtils.equals("ALL",param.getTranscodeStatus())){
                query.addCriteria(Criteria.where("transcodeStatus").ne(VideoTranscodeStatusEnum.PROCESSING.getValue()));
            } else {
                query.addCriteria(Criteria.where("transcodeStatus").is(param.getTranscodeStatus()));
            }
        }
        Object adminUser = redisUtils.get(RedisKey.SYS_THIRD_USER_ADMIN);
        if(Objects.isNull(adminUser) ||
                !StringUtils.equals(String.valueOf(adminUser), param.getThirdId())){
            query.addCriteria(Criteria.where("thirdId").is(param.getThirdId()));
        } else if (Objects.nonNull(param.getSearchStartTimeTrans()) && Objects.nonNull(param.getSearchEndTimeTrans())) {
            query.addCriteria(Criteria.where("lastModifiedDate")
                    .gte(DateUtil.timestamp2GMTLocalDateTime(param.getSearchStartTimeTrans()))
                    .lte(DateUtil.timestamp2GMTLocalDateTime(param.getSearchEndTimeTrans())));
        }
        log.info("查询直播房间列表 LiveVideosRepository params={}", query);
        PageResponse<LiveVideos> objects = mongoPageHelper.pageQuery(query, LiveVideos.class, param);
        log.info("查询直播房间列表 LiveVideosRepository params={}", query);
        PageResponse<RoomVideoVO> response = JacksonUtils.toBean(JacksonUtils.toJson(objects), PageResponse.class);
        List<RoomVideoVO> voList = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(objects) && ObjectUtils.isNotEmpty(objects.getData())){
            objects.getData().forEach(
                    data -> {
                        RoomVideoVO vo = new RoomVideoVO();
                        FastBeanUtils.copy(data, vo);
                        vo.setFileSizeGB(ConstantUtil.getFileSizeStrByByte(data.getFileSize()));
                        vo.setTranscodeTime(DateUtil.getTimeStrampSeconds(data.getLastModifiedDate()));
                        voList.add(vo);
                    }
            );
        }
        response.setData(voList);
        return response;
    }

    /**
     * 获取云点播视频上传签名
     * @return
     */
    @Override
    public String getVodUploadSign(){
        String redisKey = String.format(RedisKey.TENCENT_UPLOAD_SIGN);
        Object object = redisUtils.get(redisKey);
        String strSign = "";
        if (Objects.isNull(object)) {
            strSign = txCloudVodUtil.createVodUploadSign();
            redisUtils.set(redisKey, strSign, 2*24*60*60L);//有效期两天
            return strSign;
        }
        return String.valueOf(object);
    }

    //获取文件最大size (单位 GB)
    @Override
    public Integer getFileMaxSize(){
        String redisKey = String.format(RedisKey.UPLOAD_FILE_MAX_SIZE_GB);
        Object object = redisUtils.get(redisKey);
        if (Objects.isNull(object)) {
            redisUtils.set(redisKey, 3);
            return 3;
        }
        return (int)object;
    }

    /**
     * 保存上传视频库成功的视频信息
     */
    @SneakyThrows
    @Override
    public Boolean saveVodVideo(RoomVideoEditParam param){
        String thirdAK = accessAuthUtil.getThirdAK(param.getThirdId());
        String fileId = param.getFileId();
        if(StringUtils.isBlank(fileId) || StringUtils.isBlank(param.getVideoURL())){
            throw new BizException(ResponseCode.NULL_VIDEO_URL);
        }
        fileId = StringUtils.equals(param.getSource(),TxVideoSourceType.COS.getType())?MD5.md5(fileId):fileId;
        if(Objects.nonNull(liveVideosRepository.findTop1ByFileIdOrderByCreateDateTimeDesc(fileId))){
            return Boolean.TRUE;
        }
        String redisKey = String.format(RedisKey.LIVE_VIDEOS_FILE_ID, fileId);
        Object incr = redisUtils.incrBy(redisKey, 1L);
        if(Integer.parseInt(String.valueOf(incr))>1){
            log.info("保存上传视频库成功的视频信息：该视频已保存 redisKey={}",redisKey);
            return Boolean.TRUE;
        }
        redisUtils.expire(redisKey,3L);

        LiveVideos data = new LiveVideos();
        FastBeanUtils.copy(param, data);
        data.setAk(thirdAK);
        data.setOriginVideoURL(param.getVideoURL());
        data.setVideoURL("—");
        data.setSourceType(VideoSourceTypeEnum.UPLOAD.getValue());
        data.setTranscodeStatus(VideoTranscodeStatusEnum.NORMAL.getValue());
        data.initSave(param.getCreateUser());
        data.setFileId(fileId);
        LiveVideos save = liveVideosRepository.save(data);
        handleVideoBySource(save, param.getSource());
        return Boolean.TRUE;
    }

    @SneakyThrows
    private LiveVideos handleVideoBySource(LiveVideos video, String videoSource) {
        log.info("对不同存储源的视频进行不同的处理， video={}, videoSource={}",video,videoSource);
        videoSource = StringUtils.isBlank(videoSource)?video.getSource():videoSource;
        TxVideoSourceType sourceType = TxVideoSourceType.lookup(videoSource);
        switch (sourceType) {
            case VOD:
                video = transcodeVodVideo(video);
                break;
            case COS:
                VideoVodMessage message = VideoVodMessage.builder().userId(video.getThirdId())
                        .videoId(video.getId()).path(video.getOriginVideoURL()).tranType("2").build();
                log.info("组装自定义转码消息， message={}",message);
                rocketMQProducer.sendDelayMsg(RocketMQConstant.DELAY_TOPIC_VIDEO_TRANSCODING_START,
                        JSON.toJSONString(message),0);
                video.setTranscodeStatus(VideoTranscodeStatusEnum.PROCESSING.getValue());
                break;
            default:
                throw new BizException(TxVideoSourceType.UNKNOWN.getDesc());
        }
        video.initUpdate("");
        liveVideosRepository.save(video);
        return video;
    }

    /**
     * 录制完成 视频路径-保存
     */
    @Override
    public LiveVideos saveRecordVideo(RoomVideoEditParam param, Boolean needTranscode){
        log.info("直播录制媒体信息保存本地 param；{}",param);
        if(StringUtils.isBlank(param.getFileId()) || StringUtils.isBlank(param.getVideoURL())){
            throw new BizException(ResponseCode.NULL_VIDEO_URL);
        }
        if(StringUtils.isBlank(param.getCreateUser())){
            StreamingRoomVO detail = streamingRoomServiceImpl.detail(param.getRoomNo());
            param.setCreateUser(detail.getThirdId());
            param.setThirdId(detail.getThirdId());
            param.setAk(detail.getAk());
        }
        LiveVideos data = new LiveVideos();
        FastBeanUtils.copy(param, data);
        data.initSave(param.getCreateUser());
        data.setOriginVideoURL(param.getVideoURL());
        data.setVideoURL("—");
        data.setSourceType(VideoSourceTypeEnum.LIVE_RECORD.getValue());
        data.setTranscodeStatus(VideoTranscodeStatusEnum.PROCESSING.getValue());
        data = liveVideosRepository.save(data);
        if(needTranscode){
            TxPullStreamResponse result = txCloudVodUtil.transcodeVodVideo(param.getFileId());
            log.info("录制媒体文件需要转码......result={}",result);
            if(Objects.nonNull(result)){
                data.setTxRequestId(result.getRequestId());
                data.setTxTranscodeTaskId(result.getTaskId());
            }
            data.initUpdate(param.getCreateUser());
            liveVideosRepository.save(data);
        }
        return data;
    }

    /**
     * 删除视频库视频
     * @param id
     * @param createUser 操作员
     * @return
     */
    @Override
    public Boolean deleteVideo(String id, String createUser) {
        String[] split = id.split(",");
        for (String idOne : split) {
            Optional<LiveVideos> video = liveVideosRepository.findById(idOne);
            if(!video.isPresent()){
                //throw new BizException(ResponseCode.NOT_FIND_VIDEO);
                log.error(ResponseCode.NOT_FIND_VIDEO.getMsg());
                continue;
            }
            LiveVideos data = video.get();
            /*// todo 查询文件是否正在使用 .视频删除录播任务
            TxPullStreamResponse result = txCloudVodUtil.deleteMediaByFileId(data.getFileId());
            if(Objects.nonNull(result)){
                data.setTxRequestId(result.getRequestId());
            }*/
            data.initDel(createUser);
            liveVideosRepository.save(data);
        }
        return Boolean.TRUE;
    }
    /**
     * 删除未保存视频库视频
     * @param fileId
     * @return
     */
    @Override
    public Boolean deleteVideoByFileId(String fileId, String operate){
        LiveVideos video = liveVideosRepository.findTop1ByFileIdOrderByCreateDateTimeDesc(fileId);
        if(Objects.isNull(video)){
            txCloudVodUtil.deleteMediaByFileId(fileId,operate);
            return Boolean.TRUE;
        }
        return deleteVideo(video.getId(), operate);
    }

    /**
     * 对视频库视频进行转码
     * @param param
     * @return
     */
    @Override
    public RoomVideoVO transcodeVideo(RoomVideoEditParam param){
        log.info("视频转码参数 param={}", param);
        String fileId = param.getFileId();
        if(StringUtils.isBlank(fileId)){
            throw new BizException(ResponseCode.NULL_VIDEO_URL);
        }
        LiveVideos video = liveVideosRepository.findTop1ByFileIdOrderByCreateDateTimeDesc(fileId);
        if(Objects.isNull(video)){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        video.initUpdate(param.getCreateUser());
        video = handleVideoBySource(video, param.getSource());
        RoomVideoVO vo = new RoomVideoVO();
        FastBeanUtils.copy(video,vo);
        return vo;
    }

    private LiveVideos transcodeVodVideo(LiveVideos video){
        log.info("(VOD)视频转码参数 video={}", video);
        TxPullStreamResponse result = txCloudVodUtil.transcodeVodVideo(video.getFileId());
        if(Objects.nonNull(result)){
            video.setTxRequestId(result.getRequestId());
            video.setTxTranscodeTaskId(result.getTaskId());
            video.setTranscodeStatus(VideoTranscodeStatusEnum.PROCESSING.getValue());
            // 保存视频转码记录
            videoTranscodingRecordServiceImpl.saveVideoTranscodeRecord(video);
        }
        return video;
    }



    /**
     * 查询直播间录播视频列表
     * @param param
     * @return
     */
    @Override
    public PageResponse<RoomVideoLivesVO> videoTaskLive(StreamingRoomQueryParams param) {
        Query query = new Query();
        PageResponse<RoomVideoLivesVO> response = new PageResponse<>();
        if (ObjectTools.isEmpty(param.getRoomNo())) {
            return response;
        }
        query.addCriteria(Criteria.where("roomNo").is(param.getRoomNo()));
        Object adminUser = redisUtils.get(RedisKey.SYS_THIRD_USER_ADMIN);
        if(Objects.isNull(adminUser) ||
                !StringUtils.equals(String.valueOf(adminUser), param.getThirdId())){
            query.addCriteria(Criteria.where("thirdId").is(param.getThirdId()));
        }
        PageResponse<StreamingRoomVideoLive> objects = mongoPageHelper.pageQuery(query, StreamingRoomVideoLive.class, param);
        response = JacksonUtils.toBean(JacksonUtils.toJson(objects), PageResponse.class);
        List<RoomVideoLivesVO> voList = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(objects) && ObjectUtils.isNotEmpty(objects.getData())){
            objects.getData().forEach(
                    data -> {
                        RoomVideoLivesVO vo = new RoomVideoLivesVO();
                        FastBeanUtils.copy(data, vo);
                        vo.setFileName(data.getVideoInfo().getFileName());
                        vo.setFileId(data.getVideoInfo().getFileId());
                        voList.add(vo);
                    }
            );
        }
        response.setData(voList);
        return response;
    }




    /**
     * 添加/编辑视频录播任务
     * @param param
     * @return
     */
    @Override
    public String editVideoTask(VideoPushRoomParam param) {
        StreamingRoomVO detail = streamingRoomServiceImpl.detail(param.getRoomNo());
        if(Objects.isNull(detail) || StringUtils.equals(detail.getStatus(), RoomStatusEnum.FORBIDDEN.getValue())){
            throw new BizException(ResponseCode.ROOM_NO_NORMAL);
        }
        return StringUtils.isNotBlank(param.getId()) ?
                modifyVideoPushRoom(param) : addVideoPushRoom(param);
    }
    /** 编辑回放列表视频 - 修改时间 启用禁用 */
    private String modifyVideoPushRoom(VideoPushRoomParam param){
        log.info("更新直播间录播视频信息: param={}",param);
        Optional<StreamingRoomVideoLive> liveVideo = streamingRoomVideoLiveRepository.findById(param.getId());
        if(!liveVideo.isPresent()){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        StreamingRoomVideoLive roomVideoLive = liveVideo.get();
        /*LiveVideos video = liveVideosRepository.findTop1ByFileIdOrderByCreateDateTimeDesc(roomVideoLive.getVideoInfo().getFileId());
        if(Objects.isNull(video) || video.getDeleted()){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }*/
        String roomNo = roomVideoLive.getRoomNo();

        TxPullStreamResponse result = null;
        if(StringUtils.equals(param.getStatus(), PullStreamStatusEnum.PAUSE.getValue())){
            log.info("禁用任务 taskId:{}", roomVideoLive.getTxTaskId());
            TxPushStreamModifyParam reqParam = TxPushStreamModifyParam.builder()
                    .taskId(roomVideoLive.getTxTaskId()).operator(param.getCreateUser())
                    .status(PullStreamStatusEnum.PAUSE.getValue()).build();
            result = txCloudLiveUtil.modifyPullStreamTask(reqParam);
            roomVideoLive.setStatus(PullStreamStatusEnum.PAUSE.getValue());
        }else{
            String endTime = param.getEndTime();
            Long vodLoopTimes = -1L;//循环次数；
            if(StringUtils.isBlank(endTime)){
                endTime = DateUtil.addDateSecond(param.getStartTime(), roomVideoLive.getVideoInfo().getSeconds());
                vodLoopTimes = 0L;
            }
            TxPushStreamModifyParam reqParam = TxPushStreamModifyParam.builder()
                    .taskId(roomVideoLive.getTxTaskId()).operator(param.getCreateUser())
                    .startTime(param.getStartTime()).endTime(endTime).status(PullStreamStatusEnum.ENABLE.getValue())
                    .VodLoopTimes(vodLoopTimes)//.status(param.getStatus())
                    .build();
            result = txCloudLiveUtil.modifyPullStreamTask(reqParam);
            roomVideoLive.setStartTime(param.getStartTime());
            roomVideoLive.setEndTime(endTime);
            roomVideoLive.setStatus(PullStreamStatusEnum.TASK_START.getValue());
            updateLiveOverRoomStatus(roomNo);
        }
        roomVideoLive.initUpdate(param.getCreateUser());
        if(Objects.nonNull(result)){
            roomVideoLive.setTxRequestId(result.getRequestId());
        }
        streamingRoomVideoLiveRepository.save(roomVideoLive);
        changeRoomStatus(roomNo);
        return roomVideoLive.getPlayUrl();
    }

    /** 添加视频到回访列表 */
    private String addVideoPushRoom(VideoPushRoomParam param){
        log.info("视频添加到直播间录播列表: param={}",param);
        String roomNo = param.getRoomNo();
        String thirdId = param.getThirdId();
        String thirdAK = accessAuthUtil.getThirdAK(thirdId);
        LiveOpenVo liveOpen = tencentLiveService.liveOpen(roomNo);
        Optional<LiveVideos> videoInfo = liveVideosRepository.findById(param.getVideoId());
        if(!videoInfo.isPresent()){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        RoomVideoVO videoVO = new RoomVideoVO();
        FastBeanUtils.copy(videoInfo.get(),videoVO);
        String endTime = param.getEndTime();
        String startTime = param.getStartTime();
        String vodLoopTimes = "-1";//循环次数；
        if(StringUtils.isBlank(endTime)){
            endTime = DateUtil.addDateSecond(startTime, videoVO.getSeconds());
            vodLoopTimes = "0";
        }
        TxPushStreamParam reqParam = TxPushStreamParam.builder()
                .startTime(startTime).endTime(endTime).VodLoopTimes(vodLoopTimes)
                .urls(videoVO.getVideoURL().split(",")).operator(param.getCreateUser())
                .comment(videoVO.getFileName()).build();
        reqParam.setStreamName(liveOpen.getSecret());
        TxPullStreamResponse result = txCloudLiveUtil.pullVodStreamTask(reqParam);
        StreamingRoomVideoLive videoLive = handleRoomVideoLiveData(thirdAK,thirdId,roomNo,
                videoVO, liveOpen.getPlayUrl(),startTime, endTime,result, param.getCreateUser());
        updateLiveOverRoomStatus(roomNo);

        //更新转码记录-转码视频被使用情况
        videoTranscodingRecordServiceImpl.updateTransRecordRoomByFileId(videoVO.getFileId(),roomNo);
        return videoLive.getPlayUrl();
    }

    /**
     * 组装视频任务数据
     */
    private StreamingRoomVideoLive handleRoomVideoLiveData(String thirdAK,String thirdId,String roomNo,
                                                           RoomVideoVO videoInfo, String playUrl,
                                                           String startTime, String endTime,
                                                           TxPullStreamResponse result, String createUser){
        StreamingRoomVideoLive videoLive = StreamingRoomVideoLive.builder()
                .ak(thirdAK).thirdId(thirdId).roomNo(roomNo)
                .videoInfo(videoInfo).duration(videoInfo.getDuration()).playUrl(playUrl)
                .startTime(startTime).endTime(endTime).status(PullStreamStatusEnum.TASK_START.getValue())
                .txTaskId(result.getTaskId()).txRequestId(result.getRequestId()).build();
        videoLive.initSave(createUser);
        return streamingRoomVideoLiveRepository.save(videoLive);
    }

    /**
     * 直播结束更新房间状态
     */
    private void updateLiveOverRoomStatus(String roomNo){
        StreamingRoomVO detail = streamingRoomServiceImpl.detail(roomNo);
        if(Objects.nonNull(detail) && StringUtils.equals(detail.getStatus(), RoomStatusEnum.LIVE_OVER.getValue())){
            streamingRoomServiceImpl.updateRoomStatus(roomNo, RoomStatusEnum.LIVE_PAUSE.getValue(),"");
        }
    }


    /**
     * 删除视频录播任务
     * @param id 视频任务id
     * @return
     */
    @Override
    public Boolean deleteVideoTask(String id, String createUser) {
        Optional<StreamingRoomVideoLive> liveVideo = streamingRoomVideoLiveRepository.findById(id);
        if(!liveVideo.isPresent()){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        StreamingRoomVideoLive roomVideoLive = liveVideo.get();
        TxPushStreamModifyParam reqParam = TxPushStreamModifyParam.builder()
                .taskId(roomVideoLive.getTxTaskId()).operator(roomVideoLive.getCreateUser()).build();
        TxPullStreamResponse result = txCloudLiveUtil.delPullStreamTask(reqParam);
        if(Objects.nonNull(result)){
            roomVideoLive.setTxRequestId(result.getRequestId());
        }
        roomVideoLive.initDel(createUser);
        streamingRoomVideoLiveRepository.save(roomVideoLive);
        changeRoomStatus(roomVideoLive.getRoomNo());
        return Boolean.TRUE;
    }


    /**
     * 删除房间之后删除对录播任务
     * @param roomNo 房间号
     * @return
     */
    @Override
    public Boolean deleteVideoLiveByRoomNo(String roomNo) {
        StreamingRoomVideoLive param = StreamingRoomVideoLive.builder().roomNo(roomNo).build();
        log.info("删除房间之后删除对录播任务, param={}",param);
        param.setDeleted(Boolean.FALSE);
        List<StreamingRoomVideoLive> allVideoLive = streamingRoomVideoLiveRepository.findAll(Example.of(param));
        if(ObjectUtils.isEmpty(allVideoLive)){
            return Boolean.TRUE;
        }
        allVideoLive.forEach(
                video -> {
                    String txRequestId = "";
                    try {
                        TxPushStreamModifyParam reqParam = TxPushStreamModifyParam.builder()
                                .taskId(video.getTxTaskId()).operator(video.getCreateUser()).build();
                        TxPullStreamResponse result = txCloudLiveUtil.delPullStreamTask(reqParam);
                        if(Objects.nonNull(result)){
                            txRequestId = result.getRequestId();
                        }
                    } catch (Exception e) {
                        txRequestId = e.getMessage();
                    }
                    video.setTxRequestId(txRequestId);
                    video.initDel();
                }
        );
        streamingRoomVideoLiveRepository.saveAll(allVideoLive);
        return Boolean.TRUE;
    }





    /**
     * 根据任务id查询直播视频信息
     * @param taskId
     * @return
     */
    @Override
    public StreamingRoomVideoLive findVideoByTaskId(String taskId) {
        log.info("根据任务id查询直播视频信息,taskId={}",taskId);
        return streamingRoomVideoLiveRepository.findTop1ByTxTaskIdOrderByCreateDateTimeDesc(taskId);
    }
    /**
     * 根据任务id 更新 直播视频信息状态
     * @param taskId
     * @param status  TaskStart-任务开始
     *                VodSourceFileStart-点播文件开始
     *                VodSourceFileFinish-点播文件结束
     *                TaskExit - 任务退出
     */
    @Override
    public Boolean updateStatus(String taskId, String status) {
        log.info("更新视频任务状态： task_id={}, task_status={}", taskId, status);
        StreamingRoomVideoLive video = findVideoByTaskId(taskId);
        if(Objects.isNull(video)){
            return Boolean.FALSE;
        }
        String roomNo = video.getRoomNo();
        Long videoEndTime = DateUtil.parseYMDHMS2Mils(video.getEndTime());
        StreamingRoomVO detail = streamingRoomServiceImpl.detail(roomNo);
        if(Objects.nonNull(detail) && !StringUtils.equals(detail.getStatus(), RoomStatusEnum.FORBIDDEN.getValue())){
            PullStreamStatusEnum statusEnum = PullStreamStatusEnum.getByValue(status);
            long currentTimeStramp = DateUtil.getTimeStrampSeconds();
            switch (statusEnum){
                case TASK_START:
                    if(Objects.nonNull(videoEndTime) && videoEndTime>=currentTimeStramp ){
                        video.setStatus(PullStreamStatusEnum.START.getValue());
                        log.info("直播间：{} -开始播放视频：{} ... video status={}",
                                roomNo, video.getVideoInfo().getFileName(), video.getStatus());
                        streamingRoomServiceImpl.updateRoomStatus(roomNo, RoomStatusEnum.LIVE_ING.getValue(),"");
                        roomPlaybackServiceImpl.saveRecordVideo(video);
                    }
                    break;
                case TASK_EXIT:
                    if(Objects.nonNull(videoEndTime) && videoEndTime<=currentTimeStramp){
                        log.info("直播间：{} -结束播放视频：{} ..", roomNo, video.getVideoInfo().getFileName());
                        video.setStatus(PullStreamStatusEnum.TASK_EXIT.getValue());
                        changeRoomStatus(roomNo);
                    }
                    addRoomNo2RedisStopStream(roomNo);
                    break;
                default:
                    break;
            }
        }
        video.initUpdate("");
        log.info("更新视频任务状态： 更新视频任务信息到数据库 video={}", video);
        streamingRoomVideoLiveRepository.save(video);
        return Boolean.TRUE;
    }
    //录播视频结束，十分钟后查询房间流状态并处理
    private void addRoomNo2RedisStopStream(String roomNo){
        LiveStreamStopVO liveStreamStopVO = LiveStreamStopVO.builder()
                .roomNo(roomNo).roomStatus(RoomStatusEnum.LIVE_PAUSE.getValue())
                .handleRoomStatusTime(txCloudUtil.getLiveStopHandleTime()).build();
        log.info("录播：{}，十分钟后查询房间={}流状态并处理 直播间状态&im聊天室&录制任务&播放记录 ...."
                ,RoomStatusEnum.LIVE_PAUSE.getDesc(),roomNo);
        redisUtils.hmDel(RedisKey.STOP_STREAM_ROOM_NO, roomNo);
        redisUtils.hmSet(RedisKey.STOP_STREAM_ROOM_NO, roomNo, JSON.toJSONString(liveStreamStopVO));
    }

    /**
     * 更新直播间状态
     */
    private void changeRoomStatus(String roomNo){
        List<StreamingRoomVideoLive> data = findNoEndVideoByRoomNo(roomNo);
        log.info("查询是否还有未播放视频： result={}",data);
        if(data.isEmpty()){
            streamingRoomServiceImpl.updateRoomStatus(roomNo, RoomStatusEnum.LIVE_OVER.getValue(),"");
            return;
        }else{
            List<String> collect = data.stream().map(StreamingRoomVideoLive::getStatus).collect(Collectors.toList());
            if(collect.contains(PullStreamStatusEnum.START.getValue())){
                return;
            }
            if(collect.contains(PullStreamStatusEnum.TASK_START.getValue())){
                streamingRoomServiceImpl.updateRoomStatus(roomNo, RoomStatusEnum.LIVE_PAUSE.getValue(),"");
                return;
            }
            streamingRoomServiceImpl.updateRoomStatus(roomNo, RoomStatusEnum.LIVE_OVER.getValue(),"");
        }
   }

   private List<StreamingRoomVideoLive> findNoEndVideoByRoomNo(String roomNo){
       Query query = new Query();
       String nowTime = DateUtil.fromDate2Str(new Date());
       LiveVideosQueryParams param = new LiveVideosQueryParams();
       query.addCriteria(Criteria.where("roomNo").is(roomNo));
       query.addCriteria(Criteria.where("endTime").gte(nowTime));
       param.setRoomNo(roomNo);
       param.setEndTime(nowTime);
       log.info("查询是否还有未播放视频： param={}",param);
       PageResponse<StreamingRoomVideoLive> objects = mongoPageHelper.pageQuery(query, StreamingRoomVideoLive.class, param);
       return objects.getData();
   }


    /**
     * 根据房间状态（启用/封禁）处理视频
     * @param roomNo
     * @param roomStatus forbidden-房间封禁
     */
    @Override
    public Boolean handleVideoByRoomStatus(String roomNo, String roomStatus) {
        log.info("根据房间状态（启用/封禁）处理视频： roomNo={}， roomStatus={}.", roomNo,roomStatus);
        List<StreamingRoomVideoLive> noEndVideoList = findNoEndVideoByRoomNo(roomNo);
        if(noEndVideoList.isEmpty()){
            return Boolean.TRUE;
        }
        String videoTaskStatus = PullStreamStatusEnum.ENABLE.getValue();
        String videoDataStatus = PullStreamStatusEnum.TASK_START.getValue();
        if(StringUtils.equals(roomStatus, RoomStatusEnum.FORBIDDEN.getValue())){
            videoTaskStatus = PullStreamStatusEnum.PAUSE.getValue();
            videoDataStatus = PullStreamStatusEnum.PAUSE.getValue();
        }
        String taskStatus = videoTaskStatus;
        String videoStatus = videoDataStatus;
        noEndVideoList.forEach(
                videoTask -> {
                    TxPushStreamModifyParam reqParam = TxPushStreamModifyParam.builder()
                            .taskId(videoTask.getTxTaskId()).operator(videoTask.getCreateUser())
                            .status(taskStatus).build();
                    TxPullStreamResponse result = txCloudLiveUtil.modifyPullStreamTask(reqParam);
                    if(Objects.nonNull(result)){
                        videoTask.setTxRequestId(result.getRequestId());
                    }
                    videoTask.setStatus(videoStatus);
                    videoTask.initUpdate("");
                }
        );
        streamingRoomVideoLiveRepository.saveAll(noEndVideoList);
        return Boolean.TRUE;
    }


    /**
     * 更新录播房间所有（已到过期时间但是状态不是过期的）视频任务状态
     * @param roomNo
     * @return
     */
    @Override
    public Boolean handleVideoStatusByRoomNo(String roomNo){
        log.info("更新录播房间所有（已到过期时间但状态不是过期的）视频任务状态，roomNo={}",roomNo);
        Query query = new Query();
        query.addCriteria(Criteria.where("roomNo").is(roomNo));
        query.addCriteria(Criteria.where("endTime").lte(DateUtil.fromDate2Str(new Date())));
        query.addCriteria(Criteria.where("status").ne(PullStreamStatusEnum.TASK_EXIT.getValue()));
        Update update = new Update();
        update.set("status", PullStreamStatusEnum.TASK_EXIT.getValue());
        update.set("lastModifiedDate", ObjectTools.GetCurrentTime());
        mongoTemplate.updateMulti(query, update, StreamingRoomVideoLive.class);
        return Boolean.TRUE;
    }



}
