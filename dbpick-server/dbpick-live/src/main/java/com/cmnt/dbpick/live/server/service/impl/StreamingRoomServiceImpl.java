package com.cmnt.dbpick.live.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.config.LiveAddress;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.enums.SwitchEnum;
import com.cmnt.dbpick.common.enums.UserRoleEnum;
import com.cmnt.dbpick.common.enums.live.*;
import com.cmnt.dbpick.common.enums.tencent.TxStreamStateType;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.mq.RocketMQConfig;
import com.cmnt.dbpick.common.mq.RocketMQProducer;
import com.cmnt.dbpick.common.mq.constant.MqDelayLevelEnum;
import com.cmnt.dbpick.common.mq.constant.RocketMQConstant;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.config.TxRecordConfig;
import com.cmnt.dbpick.common.tx.tencent.TxCloudImUtil;
import com.cmnt.dbpick.common.tx.tencent.TxCloudLiveUtil;
import com.cmnt.dbpick.common.tx.tencent.request.im.ImNoticeMessageParam;
import com.cmnt.dbpick.common.tx.tencent.response.live.TxStreamStateResponse;
import com.cmnt.dbpick.common.user.RoomConfigVO;
import com.cmnt.dbpick.common.user.StreamingRoomTimeVO;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.live.api.message.RoomPlayRecordMessage;
import com.cmnt.dbpick.live.api.model.StreamingRoomInfo;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.LiveOpenVo;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.live.api.vo.RoomHotOnlineVO;
import com.cmnt.dbpick.live.api.vo.redis.LiveRecordTaskVO;
import com.cmnt.dbpick.live.api.vo.redis.LiveStreamStopVO;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoom;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomRepository;
import com.cmnt.dbpick.live.server.service.*;
import com.cmnt.dbpick.user.api.feign.PresenterClient;
import com.cmnt.dbpick.user.api.params.PresenterAddParam;
import com.cmnt.dbpick.user.api.vo.LiveUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class StreamingRoomServiceImpl implements StreamingRoomService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoPageHelper mongoPageHelper;
    @Autowired
    private RocketMQProducer rocketMQProducer;
    @Autowired
    private AccessAuthUtil accessAuthUtil;
    @Autowired
    private LiveRoomRedisUtil liveRoomRedisUtil;

    @Autowired
    private TxCloudUtil txCloudUtil;
    @Autowired
    private TxCloudImUtil txCloudImUtil;
    @Autowired
    private TxCloudLiveUtil txCloudLiveUtil;
    @Autowired
    private TencentLiveService tencentLiveServiceImpl;
    @Autowired
    private PresenterClient presenterClient;
    @Autowired
    private StreamingRoomRepository streamingRoomRepository;

    @Autowired
    private RoomConfigService roomConfigServiceImpl;
    @Lazy
    @Autowired
    private LiveVideosService liveVideosServiceImpl;
    @Lazy
    @Autowired
    private WatermarkService watermarkServiceImpl;
    @Autowired
    private LiveRoomMenuService liveRoomMenuServiceImpl;
    @Autowired
    private LiveRecordTaskLogService liveRecordTaskLogServiceImpl;
    @Autowired
    private RoomPlayRecordService roomPlayRecordServiceImpl;

    /**
     * 校验商户账号和凭证
     * @param thirdId
     * @return
     */
    @Override
    public String checkAccessKeyByThirdId(String thirdId) {
        log.info("校验商户账号: thirdId:{}",thirdId);
        String thirdAK = accessAuthUtil.getThirdAK(thirdId);
        log.info("商户 thirdAK:{}",thirdAK);
        return thirdAK;
    }
    /**
     * 生成房间号并缓存redis
     * @return
     */
    private String nestRoomNo() {
        String roomNo;
        do {
            Long idx = liveRoomRedisUtil.incRoomNo();
            roomNo = String.valueOf(10000 + idx);
            if(!EnvironmentUtil.isProd()){
//                roomNo = "test-"+roomNo;
                roomNo = String.valueOf(idx);
            }
        }
        while (Objects.nonNull(streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo)));
        return roomNo;
    }

    /**
     * 新增直播房间
     */
    @Override
    public StreamingRoomVO add(StreamingRoomParams param) {
        String ak = checkAccessKeyByThirdId(param.getThirdId());
        String roomNo = nestRoomNo();
        log.info("新增直播房间 roomNo={} params={}", roomNo, param);
        StreamingRoom room = initRoomInfo(param, roomNo, ak);

        PresenterAddParam presenterAddParam = PresenterAddParam.builder().
        roomNo(roomNo).title(room.getInfo().getTitle()).build();
        presenterAddParam.setThirdId(room.getThirdId());
        presenterAddParam.setCreateUser(room.getCreateUser());
        log.info("新增直播房间-添加主持人：presenterAddParam={}",presenterAddParam);
        ResponsePacket<LiveUserVO> addPresenterRes = presenterClient.addToRoom(presenterAddParam);
        if (!StringUtils.equals(ResponseCode.HTTP_RES_CODE_200.getCode(),addPresenterRes.getCode())){
            throw new BizException(addPresenterRes.getMsg(),ResponseCode.PRESENTER_EXIST.getCode());
        }
        handleRoomByType(roomNo, room.getType(), addPresenterRes.getData().getId());
        createPlaybackRoomIm(room);
        streamingRoomRepository.save(room);
        StreamingRoomVO roomVO = asStreamingRoomVO(room);
        refreshRoomCache(roomVO);
        initRoomMenu(roomNo, roomVO.getAnnouncement());
        return roomVO;
    }

    /** 組裝直播间参数 */
    private StreamingRoom initRoomInfo(StreamingRoomParams param, String roomNo, String ak) {
        String thirdId = param.getThirdId();
        StreamingRoom room = StreamingRoom.builder().ak(ak).thirdId(thirdId).roomNo(roomNo)
                .type(param.getType()).classify(param.getClassify()).status(RoomStatusEnum.NO_LIVE.getValue())
                .watchFilter(LiveWatchFilterTypeEnum.NO_FILTER.getValue()).visitorSwitch(SwitchEnum.CLOSE.getValue())
                .playbackSwitch(param.getPlaybackSwitch()).playbackTimeOut(param.getPlaybackTimeOut()).build();
        log.info("初始化房间信息： room={}",room);
        StreamingRoomInfo info = new StreamingRoomInfo();
        FastBeanUtils.copy(param, info);
        String recordedStatus = param.getRecordedStatus();
        if(StringUtils.equals(room.getType(), RoomTypeEnum.RECORD.getValue())){
            recordedStatus = RoomRecordedEnum.UNRECORDED.getValue();
        }
        info.setRecordedStatus(recordedStatus);
        info.setWatermarkId(-1L);
        info.setStartTime(StringUtils.isBlank(param.getStartTime())
                ?DateUtil.fromDate2Str(new Date()):param.getStartTime());

        //取默认值
        info = roomConfigServiceImpl.dealDefaultRoomConfig(info, param);
        info.setChatRoomNo(roomNo);
        LiveOpenVo vo = tencentLiveServiceImpl.getSecretByRoomNo(roomNo);
        info.setPushUrl(vo.getPushUrl()+vo.getSecret());
        info.setPushUrlTimeOut(vo.getPushUrlTimeOut());
        info.setPlayUrl(vo.getPlayUrl());
        log.info("初始化房间详情： roomInfo={}",info);
        room.setInfo(info);
        room.initSave(param.getCreateUser());
        return room;
    }

    /**
     * 根据不同房间类型做不同处理
     */
    private void handleRoomByType(String roomNo, String roomType, String presenterId) {
        RoomTypeEnum roomTypeEnum = RoomTypeEnum.getByValue(roomType);
        switch (roomTypeEnum){
            case RECORD:
            case THIRD_PUSH:
                log.info("录播&三方推流类型直播间 创建群组：roomNo={}",roomNo);
                createChatRoom(roomNo, UserInfoUtils.concatUserRoomRole(
                        presenterId,roomNo, UserRoleEnum.MAJOR.getValue()));
                break;
            case MEETING:
            case LIVE:
                log.info("直播类型直播间 默认封禁：roomNo={}, roomType={}",roomNo,roomType);
                forbidLiveStream(roomNo);
                break;
            default:
                throw new BizException("暂不支持该【"+roomTypeEnum.getDesc()+"】类型直播 ... ");
        }
    }
    private void createPlaybackRoomIm(StreamingRoom room){
        String playbackRoomIm = RoomCommonUtil.getPlaybackRoom(room.getRoomNo());
        if(StringUtils.equals(room.getPlaybackSwitch(), SwitchEnum.OPEN.getValue()) &&
                !(room.getPlaybackTimeOut() < DateUtil.getTimeStrampSeconds() && room.getPlaybackTimeOut()>0)){
            txCloudImUtil.createImGroup(room.getCreateUser(), playbackRoomIm, playbackRoomIm);
        }
    }

    /**
     * 添加默认菜单设置 - 直播介绍
     */
    private void initRoomMenu(String roomNo, String content){
        LiveRoomMenuParam menuParam = LiveRoomMenuParam.builder().roomNo(roomNo)
                .menuType("introduceMenu").menuName("introduceTab").menuTitle("直播介绍")
                .menuContent(content).build();
        log.info("添加默认菜单设置 menuParam={}", menuParam);
        liveRoomMenuServiceImpl.edit(menuParam);
    }

    /**
     * 更新直播间信息
     */
    @Override
    public StreamingRoomVO  update(StreamingRoomParams params) {
        log.info("更新直播房间 params={}", params);
        Optional<StreamingRoom> optional = streamingRoomRepository.findById(params.getId());
        if (!optional.isPresent()) {
            throw new BizException(ResponseCode.AUCTION_ILLEGAL_ROOM.getMsg(), params.getId());
        }
        StreamingRoom room = optional.get();
        StreamingRoomInfo info = room.getInfo();
        //取默认值
        String thirdId = params.getThirdId();
        RoomConfigVO roomConfig = null;
        String title = params.getTitle();
        if(StringUtils.isBlank(title)){
            roomConfig = accessAuthUtil.getRoomConfigInfo(thirdId);
            title = roomConfig.getTitle();
        }
        info.setTitle(title);
        if(StringUtils.isNotBlank(params.getSponsor())){
            info.setSponsor(params.getSponsor());
        }
        if(StringUtils.isNotBlank(params.getScene())){
            info.setScene(params.getScene());
        }
        if(StringUtils.isNotBlank(params.getLineType())){
            info.setLineType(params.getLineType());
        }
        if(StringUtils.isNotBlank(params.getWatchTPL())){
            info.setWatchTPL(params.getWatchTPL());
        }
        if(StringUtils.isNotBlank(params.getStartTime())){
            info.setStartTime(params.getStartTime());
        }
        if(StringUtils.isNotBlank(params.getAnnouncement())){
            info.setAnnouncement(params.getAnnouncement());
        }
        if(StringUtils.isNotBlank(params.getBgImg())){
            info.setBgImg(params.getBgImg());
        }
        String logoCoverStr = params.getLogoCover();
        if(StringUtils.isBlank(logoCoverStr)){
            logoCoverStr = Objects.nonNull(roomConfig) ? roomConfig.getLogoCover()
                    :accessAuthUtil.getRoomConfigInfo(thirdId).getLogoCover();
        }
        info.setLogoCover(new HashSet<>(Arrays.asList(logoCoverStr.split(","))));

        room.setInfo(info);

        if(StringUtils.isNotBlank(params.getType())){
            room.setType(params.getType());
        }
        room.initUpdate(params.getCreateUser());
        streamingRoomRepository.save(room);
        StreamingRoomVO roomVO = asStreamingRoomVO(room);
        refreshRoomCache(roomVO);
        return roomVO;
    }

    /**
     * 更新直播间状态
     * @param status 房间状态：no_live-未开播 live_ing-直播中 forbidden-房间封禁 live_pause-直播暂停 live_over-直播结束
     * @return
     */
    @Async
    @Override
    public void updateRoomStatus(String roomNo, String status, String updateUser){
        log.info("更新直播间状态 roomNo={},status={},updateUser={}",roomNo, status, updateUser);
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo);
        if (Objects.isNull(room)){
            return;
        }
        RoomStatusEnum roomStatusEnum = RoomStatusEnum.lookup(status);
        handleRoomByStatus(room, roomStatusEnum, updateUser);
    }

    /**
     *  根据直播间状态处理数据
     */
    private Boolean handleRoomByStatus(StreamingRoom room, RoomStatusEnum roomStatusEnum, String updateUser) {
        log.info("处理直播间状态, room={}, status={}", room, roomStatusEnum);
        switch (roomStatusEnum){
            case LIVE_ING:
                cancelRedisStopStream(room.getRoomNo());
                if(StringUtils.equals(room.getStatus(), RoomStatusEnum.LIVE_ING.getValue())){
                    log.info("直播中,无需更改房间状态 roomNo={}, roomStatus={}", room.getRoomNo(), room.getStatus());
                    return Boolean.TRUE;
                }
                room = updateRoomToMongoAndRedis(room,roomStatusEnum,updateUser);//更新直播间数据库状态
                createImGroup(room.getRoomNo(), room.getThirdId());//处理直播间聊天室状态
                startRecordTask(room, roomStatusEnum);//启动录制任务
                startRoomPlayRecordInfo(room);//保存直播开始记录，用于查询数据统计
                //通知回放房间已经开播
                pushStartToPlayback(room);
                break;
            case LIVE_PAUSE :
            case LIVE_OVER:
            case FORBIDDEN:
                //添加 房间号到需要 解散聊天室的 队列
                LiveStreamStopVO liveStreamStopVO = LiveStreamStopVO.builder()
                        .roomNo(room.getRoomNo()).roomStatus(roomStatusEnum.getValue())
                        .handleRoomStatusTime(txCloudUtil.getLiveStopHandleTime()).build();
                log.info("{}，十分钟后查询房间={}流状态并处理 直播间状态&im聊天室&录制任务&播放记录 ...."
                        ,roomStatusEnum.getDesc(),room.getRoomNo());
                redisUtils.hmDel(RedisKey.STOP_STREAM_ROOM_NO, room.getRoomNo());
                redisUtils.hmSet(RedisKey.STOP_STREAM_ROOM_NO, room.getRoomNo(), JSON.toJSONString(liveStreamStopVO));
                break;
            default:
                log.info("没有操作 ：{} ",roomStatusEnum.getDesc());
                break;
        }
        return Boolean.TRUE;
    }

    private StreamingRoom updateRoomToMongoAndRedis(StreamingRoom room, RoomStatusEnum roomStatusEnum, String updateUser){
        log.info("roomNo={}重新开播， 更新数据库直播间状态 ....",roomStatusEnum.getDesc());
        room.setStatus(roomStatusEnum.getValue());
        room.initUpdate(updateUser);
        StreamingRoom save = streamingRoomRepository.save(room);
        refreshRoomCache(asStreamingRoomVO(save));
        return save;
    }

    private void cancelRedisStopStream(String roomNo) {
        log.info("roomNo={}重新开播，取消【断流之后查询流状态并更新直播间状态】操作",roomNo);
        redisUtils.hmDel(RedisKey.STOP_STREAM_ROOM_NO, roomNo);
    }

    private void createImGroup(String roomNo, String thirdId) {
        log.info("roomNo={}重新开播，创建im聊天室",roomNo);
        String presenterId = "";
        try {
            RoomNoParam param = new RoomNoParam();
            param.setRoomNo(roomNo);
            param.setThirdId(thirdId);
            ResponsePacket<LiveUserVO> presenter = presenterClient.getInfoByRoomNo(param);
            log.info("查询主持人信息：presenter={}",presenter);
            if (Objects.nonNull(presenter) && Objects.nonNull(presenter.getData())){
                presenterId = UserInfoUtils.concatUserRoomRole(
                        presenter.getData().getId(),roomNo,UserRoleEnum.MAJOR.getValue());
            }
        } catch (Exception e){
            log.info("查询主持人信息失败：Exception={}",e);
        }
        txCloudImUtil.createImGroup(presenterId, roomNo, roomNo);
        //从 需要解散聊天室的 队列 中移除房间号
        redisUtils.removeSet(RedisKey.DESTROY_IM_GROUP, roomNo);
    }
    private void pushStartToPlayback(StreamingRoom room) {
        String playbackRoomNo = RoomCommonUtil.getPlaybackRoom(room.getRoomNo());
        log.info("发送im消息，通知回放房间直播已开始，playbackRoomNo={}",playbackRoomNo);
        ImNoticeMessageParam msg = ImNoticeMessageParam.builder().msgKey(playbackRoomNo).groupId(playbackRoomNo)
                .msgType(MessageTypeEnum.LIVE_START.getValue()).msgContent(room.getStatus()).build();
        txCloudImUtil.imGroupSystemPush(msg);
    }

    private void startRecordTask(StreamingRoom room, RoomStatusEnum roomStatusEnum) {
        if(!StringUtils.equals(room.getInfo().getRecordedStatus(), RoomRecordedEnum.RECORDED.getValue())){
            return;
        }
        String roomNo = room.getRoomNo();
        log.info("roomNo={}重新开播，启动直播录制任务",roomNo);
        Object recordTaskInfo = redisUtils.hmGet(RedisKey.LIVE_RECORD_TASK, roomNo);
        LiveRecordTaskVO liveRecordTask = null;
        if(Objects.isNull(recordTaskInfo)){
            RoomNoParam param = new RoomNoParam();
            param.setRoomNo(roomNo);
            param.setThirdId(room.getThirdId());
            param.setCreateUser(room.getCreateUser());
            param.setAk(room.getAk());
            liveRecordTask = liveRecordTaskLogServiceImpl.createLiveRecordTask(param);
            liveRecordTask.setRoomStatus(room.getStatus());
            log.info("启动直播间-创建录制任务 roomNo={}，task_status={}",roomNo,liveRecordTask.getRoomStatus());
            redisUtils.hmSet(RedisKey.LIVE_RECORD_TASK, roomNo,JSON.toJSONString(liveRecordTask));
        } else {
            liveRecordTask = JSONObject.toJavaObject(JSONObject.parseObject(recordTaskInfo.toString()),
                    LiveRecordTaskVO.class);
            if(!StringUtils.equals(liveRecordTask.getRoomStatus(), roomStatusEnum.getValue())){
                log.info("启动直播间-重新启动录制任务 roomNo={}, 更新录制任务状态 task_status={} -> {}",
                        roomNo,liveRecordTask.getRoomStatus(), roomStatusEnum.getValue());
                liveRecordTask.setRoomStatus(roomStatusEnum.getValue());
                redisUtils.hmSet(RedisKey.LIVE_RECORD_TASK, roomNo,JSON.toJSONString(liveRecordTask));
            }
        }
    }
    private void stopRecordTask(String roomNo, String roomStatus){
        log.info("roomNo:{} 直播暂停/结束，{} 秒后停止录制..", roomNo,TxRecordConfig.RECORD_MAX_IDLE_TIME);
        Object recordTaskInfo = redisUtils.hmGet(RedisKey.LIVE_RECORD_TASK, roomNo);
        if(Objects.nonNull(recordTaskInfo)){
            LiveRecordTaskVO exitLiveRecordTask =
                    JSONObject.toJavaObject(JSONObject.parseObject(recordTaskInfo.toString()), LiveRecordTaskVO.class);
            if(StringUtils.equals(exitLiveRecordTask.getRoomStatus(), RoomStatusEnum.LIVE_ING.getValue())){
                exitLiveRecordTask.setRoomStatus(roomStatus);
            }
            exitLiveRecordTask.setTaskStopTime(DateUtil.getTimeStrampSeconds()+TxRecordConfig.LIVE_RECORD_MAX_TIME*1000);
            redisUtils.hmSet(RedisKey.LIVE_RECORD_TASK, roomNo,JSON.toJSONString(exitLiveRecordTask));
        }
    }

    private void startRoomPlayRecordInfo(StreamingRoom room){
        log.info("roomNo={}重新开播，保存直播开始记录",room.getRoomNo());
        RoomPlayRecordParams playRecordParams = new RoomPlayRecordParams();
        FastBeanUtils.copy(room, playRecordParams);
        playRecordParams.setStartTime(DateUtil.nowDateTime(DateUtil.Y_M_D_HMS));
        roomPlayRecordServiceImpl.savePlayStartRecord(playRecordParams);
    }
    private void stopRoomPlayRecordInfo(StreamingRoom room){
        log.info("roomNo:{}直播暂停/结束，{} 后保存直播开始记录..",room.getRoomNo(), "10分钟");
        RoomPlayRecordParams playRecordParams = new RoomPlayRecordParams();
        FastBeanUtils.copy(room, playRecordParams);
        playRecordParams.setStopTime(DateUtil.nowDateTime(DateUtil.Y_M_D_HMS));
        RoomPlayRecordMessage msg
                = roomPlayRecordServiceImpl.updatePlayEndRecord(playRecordParams);
        //todo mq 延迟队列  记得修改延迟时间为10分钟
        rocketMQProducer.sendDelayMsg(RocketMQConstant.DELAY_TOPIC_ROOM_PLAY_RECORD, JSON.toJSONString(msg),
                MqDelayLevelEnum.lookup(RocketMQConfig.TOPIC_ROOM_PLAY_RECORD_DELAY_TIME).getLevel());

    }

    @Override
    public void sendRoomPlayRecordMessage(RoomPlayRecordMessage msg){
        rocketMQProducer.sendDelayMsg(RocketMQConstant.DELAY_TOPIC_ROOM_PLAY_RECORD, JSON.toJSONString(msg),
                MqDelayLevelEnum.lookup(RocketMQConfig.TOPIC_ROOM_PLAY_RECORD_DELAY_TIME).getLevel());

    }

    /**
     * 处理直播停止之后的房间状态
     */
    @Override
    public Boolean stopLiveRoomStatus(LiveStreamStopVO liveStreamStop){
        if(Objects.isNull(liveStreamStop)){
            return Boolean.FALSE;
        }
        long currentTime = DateUtil.getTimeStrampSeconds();
        if(liveStreamStop.getHandleRoomStatusTime()<=currentTime){
            log.info("查询流数据并处理直播间状态.vo={}, currentTime={}",liveStreamStop,currentTime);
            String roomNo = liveStreamStop.getRoomNo();
            TxStreamStateResponse streamStatus = txCloudLiveUtil.findStreamStatus(roomNo);
            if(Objects.nonNull(streamStatus) &&
                    !StringUtils.equals(streamStatus.getStreamState(), TxStreamStateType.ACTIVE.getType())){
                log.info("直播流状态状态={} 更新房间状态 ....",streamStatus.getStreamState());
                StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo);
                if (Objects.nonNull(room)){
                    room.setStatus(liveStreamStop.getRoomStatus());
                    StreamingRoom save = streamingRoomRepository.save(room);
                    refreshRoomCache(asStreamingRoomVO(save));
                    redisUtils.hmDel(RedisKey.STOP_STREAM_ROOM_NO, room.getRoomNo());

                    //添加 房间号到需要 解散聊天室的 队列
                    log.info("添加房间号{} 到待解散聊天室的redis数组中....",roomNo);
                    redisUtils.addSet(RedisKey.DESTROY_IM_GROUP, room.getRoomNo());
                    stopRecordTask(room.getRoomNo(), room.getStatus());
                    stopRoomPlayRecordInfo(room);
                    if(StringUtils.equals(room.getType(),RoomTypeEnum.RECORD.getValue())){
                        liveVideosServiceImpl.handleVideoStatusByRoomNo(roomNo);
                    }

                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 删除直播房间
     * @param roomId
     * @return
     */
    @Override
    public boolean deleteById(String roomId, String createUser) {
        Optional<StreamingRoom> optional = streamingRoomRepository.findById(roomId);
        if (optional.isPresent()) {
            StreamingRoom streamingRoom = optional.get();
            streamingRoom.setStatus(RoomStatusEnum.FORBIDDEN.getValue());
            streamingRoom.initDel(createUser);
            StreamingRoom save = streamingRoomRepository.save(streamingRoom);
            refreshRoomCache(asStreamingRoomVO(save));
            handleRoomOtherInfo(streamingRoom);
        }
        return Boolean.TRUE;
    }
    @Async
    public void handleRoomOtherInfo(StreamingRoom streamingRoom) {
        log.info("删除直播房间完成，删除对应聊天室和视频文件");
        //destroyChatRoom(streamingRoom.getRoomNo());
        //添加 房间号到需要 解散聊天室的 队列
        redisUtils.addSet(RedisKey.DESTROY_IM_GROUP, streamingRoom.getRoomNo());
        stopRoomPlayRecordInfo(streamingRoom);
        forbidLiveStream(streamingRoom.getRoomNo());
        liveVideosServiceImpl.deleteVideoLiveByRoomNo(streamingRoom.getRoomNo());
        Long watermarkId = streamingRoom.getInfo().getWatermarkId();
        if(Objects.nonNull(watermarkId) && !Objects.equals(watermarkId, -1L)){
            log.info("取消直播间水印: roomNo={}, watermarkId={}", streamingRoom.getRoomNo(), watermarkId);
            LiveRoomWatermarkParam watermarkParam = LiveRoomWatermarkParam.builder()
                    .roomNo(streamingRoom.getRoomNo()).watermarkId(watermarkId).build();
            watermarkParam.setThirdId(streamingRoom.getThirdId());
            watermarkParam.setCreateUser(streamingRoom.getLastModifiedUser());
            watermarkServiceImpl.cancelLive(watermarkParam);
        }
    }

    /**
     * 直播房间列表
     * @param param
     * @return
     */
    @Override
    public PageResponse<StreamingRoomVO> list(StreamingRoomQueryParams param) {
        Query query = new Query();
        if(StringUtils.isNotBlank(param.getRoomNo())){
            query.addCriteria(Criteria.where("roomNo").regex(param.getRoomNo()));
        }
        if(StringUtils.isNotBlank(param.getRoomTitle())){
            query.addCriteria(Criteria.where("info.title").regex(param.getRoomTitle()));
        }
        if(StringUtils.isNotBlank(param.getStatus())){
            query.addCriteria(Criteria.where("status").is(param.getStatus()));
        }

        if (StringUtils.isNotBlank(param.getSearchStartTime()) && StringUtils.isNotBlank(param.getSearchEndTime())) {
            query.addCriteria(Criteria.where("info.startTime")
                    .gte(param.getSearchStartTime()).lte(param.getSearchEndTime()));
        }
        if (Objects.nonNull(param.getCreateStartTime()) && Objects.nonNull(param.getCreateEndTime())) {
            query.addCriteria(Criteria.where("createDateTime")
                    .gte(param.getCreateStartTime()).lte(param.getCreateEndTime()));
        }
        Object adminUser = redisUtils.get(RedisKey.SYS_THIRD_USER_ADMIN);
        if(Objects.isNull(adminUser) ||
                !StringUtils.equals(String.valueOf(adminUser), param.getThirdId())){
            query.addCriteria(Criteria.where("thirdId").is(param.getThirdId()));
        }
        log.info("查询直播房间列表 streamingRoomRepository params={}", query);
        PageResponse<StreamingRoom> list = mongoPageHelper.pageQuery(query, StreamingRoom.class, param);
        PageResponse<StreamingRoomVO> response = JacksonUtils.toBean(JacksonUtils.toJson(list), PageResponse.class);

        List<StreamingRoomVO> voList = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(list) && ObjectUtils.isNotEmpty(list.getData())){
            list.getData().forEach(room -> voList.add(asStreamingRoomVO(room)));
        }
        response.setData(voList);
        return response;
    }

    /**
     * 查询用户所有直播房间号列表
     */
    @Override
    public List<String> listByThird(ThirdRoomQueryParam param){
        log.info("查询用户所有直播房间号列表, param={}",param);
        StreamingRoomQueryParams roomQueryParam = new StreamingRoomQueryParams();
        FastBeanUtils.copy(param,roomQueryParam);
        Query query = new Query();
        query.addCriteria(Criteria.where("thirdId").is(param.getThirdId()));
        if (StringUtils.isNotBlank(param.getSearchStartTime()) && StringUtils.isNotBlank(param.getSearchEndTime())) {
            query.addCriteria(Criteria.where("info.startTime")
                    .gte(param.getSearchStartTime()).lte(param.getSearchEndTime()));
        }
        log.info("查询用户所有直播房间号列表 listByThird.query={}", query);
        roomQueryParam.setPageSize(15);
        PageResponse<StreamingRoom> list = mongoPageHelper.pageQuery(query, StreamingRoom.class, roomQueryParam);

        List<String> roomNoList = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(list) && ObjectUtils.isNotEmpty(list.getData())){
            roomNoList = list.getData().stream().map(StreamingRoom::getRoomNo).collect(Collectors.toList());
        }
        return roomNoList;
    }

    /**
     * 查询房间详情
     * @param roomNo
     * @return
     */
    @Override
    public StreamingRoomVO detail(String roomNo) {
        StreamingRoomVO roomVO = getByRoomNo(roomNo);
        if(Objects.isNull(roomVO)){
            throw new BizException(ResponseCode.AUCTION_ROOM_NULL);
        }
        roomVO.setPushUrl("");
        roomVO.setPushUrlTimeOut("");
        return roomVO;
    }
    public StreamingRoomVO getByRoomNo(String roomNo) {
        StreamingRoomVO roomVO = liveRoomRedisUtil.getInfoByRoomNo(roomNo);
        if (Objects.isNull(roomVO)) {
            StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo);
            if (Objects.nonNull(room)) {
                roomVO = asStreamingRoomVO(room);
                refreshRoomCache(roomVO);
            }
        }
        return roomVO;
    }

    /**
     * 根据id查询直播间详情
     */
    @Override
    public StreamingRoomVO info(String id) {
        Optional<StreamingRoom> byId = streamingRoomRepository.findById(id);
        if(!byId.isPresent()){
            throw new BizException(ResponseCode.AUCTION_ROOM_NULL);
        }
        StreamingRoom data = byId.get();
        StreamingRoomVO vo = asStreamingRoomVO(data);
        vo.setWatchUrl(LiveAddress.WATCH_ADDRESS+vo.getRoomNo());
        return vo;
    }

    private StreamingRoomVO asStreamingRoomVO(StreamingRoom room) {
        StreamingRoomVO vo = new StreamingRoomVO();
        FastBeanUtils.copy(room.getInfo(), vo);
        vo.setId(room.getId());
        vo.setThirdId(room.getThirdId());
        vo.setRoomNo(room.getRoomNo());
        vo.setStatus(room.getStatus());
        vo.setClassify(room.getClassify());
        vo.setType(room.getType());
        vo.setWatchFilter(room.getWatchFilter());
        vo.setVisitorSwitch(room.getVisitorSwitch());
        vo.setPlaybackSwitch(room.getPlaybackSwitch());
        vo.setPlaybackTimeOut(room.getPlaybackTimeOut());
        vo.setCreateDateTime(room.getCreateDateTime());
        return vo;
    }

    @Override
    public void refreshRoomCache(StreamingRoomVO room) {
        log.info("刷新直播房间信息 room={}", room);
        liveRoomRedisUtil.setRoomInfo(room);
    }
    @Override
    public void refreshRoomCache(StreamingRoom room) {
        log.info("刷新直播房间信息 room={}", room);
        liveRoomRedisUtil.setRoomInfo(asStreamingRoomVO(room));
    }

    /**
     * 更新房间推流地址
     */
    @Override
    public StreamingRoomVO refreshRoomPushUrl(String roomNo){
        log.info("更新房间推流地址 roomNo={}", roomNo);
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo);
        if(Objects.isNull(room)){
            return null;
        }
        LiveOpenVo vo = tencentLiveServiceImpl.getSecretByRoomNo(roomNo);
        StreamingRoomInfo info = room.getInfo();
        info.setPushUrl(vo.getPushUrl()+vo.getSecret());
        info.setPushUrlTimeOut(vo.getPushUrlTimeOut());
        room.setInfo(info);
        room.initUpdate("");
        streamingRoomRepository.save(room);
        StreamingRoomVO roomVO = asStreamingRoomVO(room);
        refreshRoomCache(roomVO);
        return roomVO;
    }

    /**
     * 开启三方推流
     */
    @Override
    public Boolean openThirdPush(String roomNo){
        log.info("开启三方推流... roomNo:{}",roomNo);
        /*StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo);
        if(Objects.isNull(room)){
            return null;
        }
        txCloudLiveUtil.txResumeStream(roomNo);
        String presenterId = "";
        RoomNoParam param = new RoomNoParam();
        param.setRoomNo(roomNo);
        param.setThirdId(room.getThirdId());
        ResponsePacket<LiveUserVO> presenter = presenterClient.getInfoByRoomNo(param);
        log.info("查询主持人信息：presenter={}",presenter);
        if (Objects.nonNull(presenter) && Objects.nonNull(presenter.getData())){
            presenterId = UserInfoUtils.concatUserRoom(presenter.getData().getId(),roomNo);
        }
        txCloudImUtil.createImGroup(presenterId, roomNo, roomNo);*/
        return Boolean.TRUE;
    }

    /**
     * 强制开启直播
     */
    @Override
    public Boolean startRoomLiveIng(String roomNo, String userId){
        updateRoomStatus(roomNo, RoomStatusEnum.LIVE_ING.getValue(),userId);
        return Boolean.TRUE;
    }

    @Override
    public boolean createChatRoom(String roomNo,String userId) {
        txCloudImUtil.imCreateGroup(userId, roomNo, roomNo);
        return Boolean.TRUE;
    }
    @Override
    public boolean destroyChatRoom(String roomNo) {
        txCloudImUtil.imDestroyGroup(roomNo);
        return Boolean.TRUE;
    }

    /**
     * 禁言房间
     * @param param
     * @return
     */
    @Override
    public Boolean silencedChatRoom(RoomSilencedParam param) {
        log.info("禁言房间,参数 param={}",param);
        SwitchEnum switchenum = SwitchEnum.getByValue(param.getSilencedSwitch());
        txCloudImUtil.imGroupAllMemberSilenced(param.getRoomNo(),switchenum);
        txCloudImUtil.imGroupAllMemberSilenced(RoomCommonUtil.getPlaybackRoom(param.getRoomNo()),switchenum);
        return Boolean.TRUE;
    }

    /**
     * 启动直播间
     * @param roomNo
     * @param createUser 操作员
     * @return
     */
    @Override
    public boolean resumeRoom(String roomNo, String createUser) {
        log.info("启动直播间 roomNo={} ", roomNo);
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo);
        if(Objects.isNull(room)){
            return Boolean.FALSE;
        }
        txCloudLiveUtil.txResumeStream(roomNo);
        if(StringUtils.equals(room.getStatus(),RoomStatusEnum.FORBIDDEN.getValue())){
            room.setStatus(RoomStatusEnum.NO_LIVE.getValue());
            room.initUpdate(createUser);
            StreamingRoom save = streamingRoomRepository.save(room);
            refreshRoomCache(asStreamingRoomVO(save));
            if(StringUtils.equals(room.getType(),RoomTypeEnum.RECORD.getValue())){
                liveVideosServiceImpl.handleVideoByRoomStatus(roomNo,room.getStatus());
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 关闭直播间
     * @param roomNo
     * @param createUser 操作员
     * @return
     */
    @Override
    public boolean dropRoom(String roomNo, String createUser) {
        log.info("关闭直播间 roomNo={} ", roomNo);
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo);
        if(Objects.isNull(room)){
            return Boolean.FALSE;
        }
        forbidLiveStream(roomNo);
        room.setStatus(RoomStatusEnum.FORBIDDEN.getValue());
        room.initUpdate(createUser);
        StreamingRoom save = streamingRoomRepository.save(room);
        refreshRoomCache(asStreamingRoomVO(save));
        if(StringUtils.equals(room.getType(),RoomTypeEnum.RECORD.getValue())){
            liveVideosServiceImpl.handleVideoByRoomStatus(roomNo,room.getStatus());
        }
        //添加 房间号到需要 解散聊天室的 队列
        redisUtils.addSet(RedisKey.DESTROY_IM_GROUP, room.getRoomNo());
        stopRecordTask(room.getRoomNo(), RoomStatusEnum.FORBIDDEN.getValue());
        stopRoomPlayRecordInfo(room);
        return Boolean.TRUE;
    }
    @Override
    public void forbidLiveStream(String roomNo) {
        txCloudLiveUtil.txForbidLiveStream(roomNo);
    }

    /**
     * 更新直播间水印
     * @param watermarkId 水印id
     * @return
     */
    @Override
    public boolean updateRoomWatermark(String roomNo, Long watermarkId, String updateUser){
        log.info("更新直播间水印roomNo={},watermarkId={},updateUser={}",roomNo, watermarkId, updateUser);
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo);
        if (Objects.isNull(room)){
            return Boolean.FALSE;
        }
        StreamingRoomInfo info = room.getInfo();
        info.setWatermarkId(watermarkId);
        room.initUpdate(updateUser);
        StreamingRoom save = streamingRoomRepository.save(room);
        refreshRoomCache(asStreamingRoomVO(save));
        return Boolean.TRUE;
    }

    /**
     * 编辑直播间观看限制
     * @param param
     * @return
     */
    @Override
    public Boolean editWatchFilter(RoomWatchFilterParam param){
        log.info("编辑直播间观看限制 param={}",param);
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(param.getRoomNo());
        if (Objects.isNull(room)){
            return Boolean.FALSE;
        }
        if(StringUtils.isNotBlank(param.getFilterType())){
            room.setWatchFilter(param.getFilterType());
        }
        if(StringUtils.isNotBlank(param.getVisitorSwitch())){
            room.setVisitorSwitch(param.getVisitorSwitch());
        }
        room.initUpdate(param.getCreateUser());
        StreamingRoom save = streamingRoomRepository.save(room);
        refreshRoomCache(asStreamingRoomVO(save));
        return Boolean.TRUE;
    }

    @Override
    public String pushAdminRedis(String thirdId) {
        boolean set = redisUtils.set(RedisKey.SYS_THIRD_USER_ADMIN, thirdId);
        return thirdId+" set "+set;
    }

    /**
     * 查询多个房间信息
     */
    @Override
    public List<StreamingRoomTimeVO> getInfoByRoomNos(String roomNos) {
        List<StreamingRoomTimeVO> voList = new ArrayList<>();
        String[] roomNoSplit = roomNos.split(",");
        Query query = new Query();
        query.addCriteria(Criteria.where("roomNo").in(roomNoSplit));
        PageResponse<StreamingRoom> objects = mongoPageHelper.pageQuery(
                query, StreamingRoom.class, new StreamingRoomQueryParams());
        objects.getData().forEach(
                room -> voList.add(StreamingRoomTimeVO.builder().roomNo(room.getRoomNo())
                        .createDate(room.getCreateDate()).lastModifiedDate(room.getLastModifiedDate()).build())
        );
        return voList;

    }

    /**
     * 更新直播间热度和在线人数
     */
    @Override
    public Boolean updRoomHotAndOnline(RoomHotOnlineVO param){
        log.info("更新直播间热度和在线人数，param={}",param);
        if(Objects.isNull(param)){
            return Boolean.TRUE;
        }
        String roomNo = param.getRoomNo();
        String realRoomNo = RoomCommonUtil.getRoomWithPlayback(param.getRoomNo());
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(realRoomNo);
        if (Objects.nonNull(room)){
            StreamingRoomInfo info = room.getInfo();
            Integer nowHotScores = info.getHotScores()+param.getHotScores();
            Integer nowOnline = info.getOnline()+param.getOnline();
            nowOnline = nowOnline>0?nowOnline:0;
            mongoTemplate.findAndModify(
                    new Query(Criteria.where("roomNo").is(realRoomNo)),
                    new Update().set("lastModifiedDate", ObjectTools.GetCurrentTime())
                            .inc("info.hotScores", param.getHotScores())
                            .inc("info.online", nowOnline>0?param.getOnline():0), StreamingRoom.class);
            info.setHotScores(nowHotScores);
            info.setOnline(nowOnline);
            log.info("更新redis直播间热度和在线人数，info={}",info);
            room.setInfo(info);
            refreshRoomCache(asStreamingRoomVO(room));
            /*sendImHotMsg(RoomHotOnlineVO.builder().roomNo(roomNo)
                    .hotScores(nowHotScores).online(nowOnline).build());*/
            sendImHotMsg(roomNo);
            updateThirdRedisHot(room.getCreateUser());
        }
        return Boolean.TRUE;
    }

    // 判断redis, 发送mq延迟，mq发送im
    private void sendImHotMsg(String roomNo){
        String redisKey = String.format(RedisKey.LIVE_ROOM_HOT_ONLINE, roomNo);
        Object aLong = redisUtils.incrBy(redisKey, 1L);
        if(Integer.parseInt(String.valueOf(aLong))>1){
            log.info("1s内已发送mq并同步热度值消息：key={},value={}",redisKey, aLong);
            return;
        }
        redisUtils.expire(redisKey,1L);
//        rocketMQProducer.sendDelayMsg(RocketMQConfig.TOPIC_ROOM_HOT_SCORES, roomNo,
//                MqDelayLevelEnum.lookup(RocketMQConfig.TOPIC_ROOM_HOT_SCORES_DELAY_TIME).getLevel());

        /*ImNoticeMessageParam msg=ImNoticeMessageParam.builder().groupId(vo.getRoomNo())
                .msgKey(vo.getRoomNo()).msgType(MessageTypeEnum.HOT_ONLINE.getValue()).msgContent(vo).build();
        log.info("准备发送im热度消息，msg={}",msg);
        txCloudImUtil.imGroupSystemPush(msg);*/
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(RoomCommonUtil.getRoomWithPlayback(roomNo));
        Integer hotScores = (Objects.nonNull(room) && Objects.nonNull(room.getInfo().getHotScores()))
                ?room.getInfo().getHotScores():0;
        Integer online = (Objects.nonNull(room) && Objects.nonNull(room.getInfo().getOnline()))
                ?room.getInfo().getOnline():0;
        RoomHotOnlineVO vo = RoomHotOnlineVO.builder().roomNo(roomNo)
                .online(online).hotScores(hotScores).build();
        ImNoticeMessageParam msg = ImNoticeMessageParam.builder().msgKey(vo.getRoomNo())
                .groupId(vo.getRoomNo()).msgContent(vo).msgType(MessageTypeEnum.HOT_ONLINE.getValue())
                .build();
        log.info("准备发送im热度消息，msg={}",msg);
        txCloudImUtil.imGroupSystemPush(msg);
    }


    /**
     * 更新用户热度值
     * @param thirdId 商户id
     */
    private void updateThirdRedisHot(String thirdId) {
        String redisKey = String.format(RedisKey.LIVE_USER_HOT_ONLINE, thirdId);
        redisUtils.incrBy(redisKey, 1L);
    }


    /**
     * 查询用户热度值
     * @param thirdId 商户id
     */
    @Override
    public String getThirdRedisHot(String thirdId){
        String redisKey = String.format(RedisKey.LIVE_USER_HOT_ONLINE, thirdId);
        Object hot = redisUtils.get(redisKey);
        log.info("查询用户热度值：redisKey={}, value={}",redisKey, hot);
        if(Objects.isNull(hot)){
            return initThirdRedisHot(thirdId);
        }
        return String.valueOf(hot);
    }

    /**
     * 初始化用户热度值
     */
    @Override
    public String initThirdRedisHot(String thirdId){
        String redisKey = String.format(RedisKey.LIVE_USER_HOT_ONLINE, thirdId);
        StreamingRoom param = StreamingRoom.builder().thirdId(thirdId).build();
        param.setDeleted(Boolean.FALSE);
        List<StreamingRoom> allRoom = streamingRoomRepository.findAll(Example.of(param));
        if(Objects.isNull(allRoom) || allRoom.isEmpty()){
            return "0";
        }
        List<Integer> hotList = new ArrayList<>();
        allRoom.forEach(roomInfo -> hotList.add(roomInfo.getInfo().getHotScores()));
        long hotSum = hotList.stream().reduce(Integer::sum).orElse(0);
        log.info("初始化用户热度值：redisKey={}, value={}",redisKey, hotSum);
        redisUtils.set(redisKey,hotSum);
        return String.valueOf(hotSum);
    }


}
