package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.enums.SortEnum;
import com.cmnt.dbpick.common.enums.SwitchEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.tx.tencent.TxCloudImUtil;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.RoomPlaybackInfoVO;
import com.cmnt.dbpick.live.api.vo.RoomPlaybackVO;
import com.cmnt.dbpick.live.api.vo.RoomVideoVO;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoom;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomPlaybackVideo;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomVideoLive;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomPlaybackVideoRepository;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomRepository;
import com.cmnt.dbpick.live.server.service.RoomPlaybackService;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;


@Slf4j
@Service
public class RoomPlaybackServiceImpl implements RoomPlaybackService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MongoPageHelper mongoPageHelper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TxCloudImUtil txCloudImUtil;

    @Autowired
    private StreamingRoomPlaybackVideoRepository streamingRoomPlaybackVideoRepository;
    @Autowired
    private StreamingRoomRepository streamingRoomRepository;

    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;

    /**
     * 房间回放列表
     */
    @Override
    public PageResponse<RoomPlaybackVO> list(StreamingRoomQueryParams param) {
        PageResponse<RoomPlaybackVO> response = new PageResponse<>();
        if (ObjectTools.isEmpty(param.getRoomNo())) {
            return response;
        }
        Query query = new Query();
        if(StringUtils.isNotBlank(param.getRoomNo())){
            query.addCriteria(Criteria.where("roomNo").is(param.getRoomNo()));
        }
        query.addCriteria(Criteria.where("deleted").is(Boolean.FALSE));
        query.with(Sort.by(Sort.Direction.ASC, new String[]{"playSort"}));
        log.info("查询直播间回放视频列表 params={}", query);
        PageResponse<StreamingRoomPlaybackVideo> list =
                mongoPageHelper.pageQuery(query, StreamingRoomPlaybackVideo.class, Function.identity(), param);
        response = JacksonUtils.toBean(JacksonUtils.toJson(list), PageResponse.class);
        List<RoomPlaybackVO> voList = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(list) && ObjectUtils.isNotEmpty(list.getData())){
            list.getData().forEach(data -> voList.add(roomPlaybackVideoAsVO(data)));
        }
        response.setData(voList);
        return response;
    }

    private RoomPlaybackVO roomPlaybackVideoAsVO(StreamingRoomPlaybackVideo playbackVideo){
        RoomPlaybackVO vo = new RoomPlaybackVO();
        FastBeanUtils.copy(playbackVideo.getVideoInfo(), vo);
        vo.setId(playbackVideo.getId());
        vo.setPlayFileName(playbackVideo.getPlayFileName());
        vo.setPlayFileName(playbackVideo.getPlayFileName());
        vo.setPlaySort(playbackVideo.getPlaySort());
        vo.setCreateDateTime(playbackVideo.getCreateDateTime());
        return vo;
    }
    /**
     * 编辑回放开关
     */
    @Override
    public Boolean editPlayback(RoomPlaybackParam param) {
        log.info("编辑回放开关设置 editPlayback ：param={}", param);
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(param.getRoomNo());
        if(StringUtils.isBlank(param.getRoomNo()) || Objects.isNull(room)){
            return Boolean.FALSE;
        }
        Boolean updStatus = Boolean.FALSE;
        if(StringUtils.isNotBlank(param.getPlaybackSwitch())
                && !StringUtils.equals(param.getPlaybackSwitch(), room.getPlaybackSwitch())){
            room.setPlaybackSwitch(param.getPlaybackSwitch());
            updStatus = Boolean.TRUE;

            String playbackRoomIm = RoomCommonUtil.getPlaybackRoom(room.getRoomNo());
            if(StringUtils.equals(room.getPlaybackSwitch(), SwitchEnum.OPEN.getValue())){
                //txCloudImUtil.createImGroup(room.getCreateUser(), playbackRoomIm, playbackRoomIm);
                txCloudImUtil.imCreateGroup(room.getCreateUser(), playbackRoomIm, playbackRoomIm);
            } else if(StringUtils.equals(room.getPlaybackSwitch(), SwitchEnum.CLOSE.getValue())) {
                txCloudImUtil.destroyImGroup(playbackRoomIm);
            }
        }
        if(Objects.nonNull(param.getPlaybackTimeOut())
                && Long.compare(room.getPlaybackTimeOut(),param.getPlaybackTimeOut())!=0){
            room.setPlaybackTimeOut(param.getPlaybackTimeOut());
            updStatus = Boolean.TRUE;
        }
        if(updStatus){
            room.initUpdate(param.getCreateUser());
            StreamingRoom save = streamingRoomRepository.save(room);
            streamingRoomServiceImpl.refreshRoomCache(save);
        }
        return Boolean.TRUE;
    }

    /**
     * 保存录播任务视频到回放列表
     */
    @Async
    @Override
    public void saveRecordVideo(StreamingRoomVideoLive videoTask) {
        log.info("保存录播任务视频到回放列表 videoTask:{}",videoTask);
        String redisKey = String.format(
                RedisKey.LIVE_ROOM_PLAYBACK_VIDEOS, videoTask.getRoomNo(),videoTask.getVideoInfo().getFileId());
        Object aLong = redisUtils.incrBy(redisKey, 1L);
        if(Integer.parseInt(String.valueOf(aLong))>1){
            log.info("该录播视频已经添加到直播间：key={},value={}",redisKey, aLong);
            return;
        }
        RoomVideoVO videoInfo = videoTask.getVideoInfo();
        RoomVideoEditParam videoParam = new RoomVideoEditParam();
        FastBeanUtils.copy(videoInfo, videoParam);
        videoParam.setVideoURL(videoInfo.getOriginVideoURL());
        if(StringUtils.isBlank(videoParam.getFileId()) || StringUtils.isBlank(videoParam.getVideoURL())){
            log.info("视频信息不能为空!！,videoParam={}",videoParam);
            throw new BizException(ResponseCode.NULL_VIDEO_URL);
        }
        videoParam.setRoomNo(videoTask.getRoomNo());
        videoParam.setCreateUser(videoTask.getThirdId());
        videoParam.setThirdId(videoTask.getThirdId());
        videoParam.setAk(videoTask.getAk());
        addRoomPlaybackVideo(videoParam);
    }

    /**
     * 保存直播录制视频到回放列表
     */
    @Async
    @Override
    public void saveRecordVideo(RoomVideoEditParam param) {
        log.info("保存直播录制视频到回放列表 param；{}",param);
        if(StringUtils.isBlank(param.getFileId()) || StringUtils.isBlank(param.getVideoURL())){
            throw new BizException(ResponseCode.NULL_VIDEO_URL);
        }
        if(StringUtils.isBlank(param.getCreateUser())
                || StringUtils.isBlank(param.getThirdId())
                || StringUtils.isBlank(param.getAk())){
            StreamingRoomVO detail = streamingRoomServiceImpl.detail(param.getRoomNo());
            param.setCreateUser(detail.getThirdId());
            param.setThirdId(detail.getThirdId());
            param.setAk(detail.getAk());
        }
        addRoomPlaybackVideo(param);
    }

    /**
     * 保存直播录制视频到回放列表数据库
     */
    @Override
    public StreamingRoomPlaybackVideo addRoomPlaybackVideo(RoomVideoEditParam param){
        RoomVideoVO vo = new RoomVideoVO();
        FastBeanUtils.copy(param,vo);
        String roomNo = param.getRoomNo();
        StreamingRoomPlaybackVideo data = StreamingRoomPlaybackVideo.builder()
                .roomNo(roomNo).playFileName(vo.getFileName()).videoInfo(vo)
                .playSort(getMaxPlaySort(roomNo)+1).build();
        data.setAk(param.getAk());
        data.setThirdId(param.getThirdId());
        data.initSave(param.getCreateUser());
        log.info("保存直播录制视频到回放列表数据库, data={}",data);
        data = streamingRoomPlaybackVideoRepository.save(data);
        return data;
    }

    private Integer getMaxPlaySort(String roomNo){
        StreamingRoomPlaybackVideo maxPlaySort =
                streamingRoomPlaybackVideoRepository.findTop1ByRoomNoOrderByPlaySortDesc(roomNo);
        if(Objects.isNull(maxPlaySort)){
            return 0;
        }
        return maxPlaySort.getPlaySort();
    }

    /**
     * 商户端添加回放视频
     * @param videosParam
     * @return
     */
    @Override
    public Boolean pushVideos(RoomPlaybackVideosParam videosParam){
        log.info("商户端添加回放视频 videosParam={}", videosParam);
        List<RoomVideoEditParam> videoList = videosParam.getVideoList();
        if(Objects.isNull(videoList) || videoList.isEmpty()){
            return Boolean.FALSE;
        }
        String roomNo = videosParam.getRoomNo();
        videoList.forEach(
                video->{
                    String redisKey = String.format(RedisKey.LIVE_ROOM_PLAYBACK_VIDEOS, roomNo,video.getFileId());
                    Object aLong = redisUtils.incrBy(redisKey, 1L);
                    if(Integer.parseInt(String.valueOf(aLong))>1){
                        log.info("该录播视频已经添加到直播间：key={},value={}",redisKey, aLong);
                        return;
                    }
                    video.setVideoURL(video.getOriginVideoURL());
                    if(StringUtils.isBlank(video.getFileId()) || StringUtils.isBlank(video.getVideoURL())){
                        log.info("视频信息不能为空!！,videoParam={}",video);
                        return;
                    }
                    video.setRoomNo(roomNo);
                    addRoomPlaybackVideo(video);
                }
        );
        return Boolean.TRUE;
    }


    /**
     * 编辑回放视频名称
     */
    @Override
    public RoomPlaybackVO editPlayName(RoomPlaybackNameParam param){
        log.info("编辑回放视频名称 editPlayName param={}", param);
        if(Objects.isNull(param) || StringUtils.isBlank(param.getId())){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        Optional<StreamingRoomPlaybackVideo> optional =
                streamingRoomPlaybackVideoRepository.findById(param.getId());
        if(!optional.isPresent()){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        StreamingRoomPlaybackVideo playbackVideo = optional.get();
        if(StringUtils.isNotBlank(param.getPlayFileName()) &&
                !StringUtils.equals(playbackVideo.getPlayFileName(),param.getPlayFileName())){
            playbackVideo.setPlayFileName(param.getPlayFileName());
            playbackVideo.initUpdate(param.getCreateUser());
            log.info("更新回放视频名称 playbackVideo={}", playbackVideo);
            playbackVideo = streamingRoomPlaybackVideoRepository.save(playbackVideo);
        }
        return roomPlaybackVideoAsVO(playbackVideo);
    }

    /**
     * 编辑回放视频顺序
     */
    @Override
    public Boolean editPlaySort(RoomPlaybackSortParam param) {
        log.info("编辑回放视频顺序 editPlaySort param={}", param);
        if(Objects.isNull(param)){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        Optional<StreamingRoomPlaybackVideo> optional =
                streamingRoomPlaybackVideoRepository.findById(param.getId());
        if(!optional.isPresent()){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        StreamingRoomPlaybackVideo playbackVideo = optional.get();
        Integer currentSort = playbackVideo.getPlaySort();
        String roomNo = playbackVideo.getRoomNo();

        StreamingRoomPlaybackVideo otherVideo = null;
        SortEnum sortEnum = SortEnum.getByValue(param.getHandleSort());
        switch (sortEnum) {
            case UPWARD:
                otherVideo = streamingRoomPlaybackVideoRepository.findTop1ByRoomNoAndDeletedAndPlaySortLessThanOrderByPlaySortDesc(
                        roomNo, Boolean.FALSE, currentSort);
                if(Objects.isNull(otherVideo)){
                    throw new BizException("当前视频已排序在第一位！");
                }
                break;
            case DOWNWARD:
                otherVideo = streamingRoomPlaybackVideoRepository.findTop1ByRoomNoAndDeletedAndPlaySortGreaterThanOrderByPlaySortAsc(
                        roomNo, Boolean.FALSE, currentSort);
                if(Objects.isNull(otherVideo)){
                    throw new BizException("当前视频已排序在最后一位！");
                }
                break;
            default:
                throw new BizException("操作类型错误!");
        }
        playbackVideo.setPlaySort(otherVideo.getPlaySort());
        playbackVideo.initUpdate("");
        streamingRoomPlaybackVideoRepository.save(playbackVideo);
        otherVideo.setPlaySort(currentSort);
        otherVideo.initUpdate("");
        streamingRoomPlaybackVideoRepository.save(otherVideo);
        return Boolean.TRUE;
    }


    /**
     * 删除回放视频
     */
    @Override
    public Boolean deletePlaybackVideo(String id, String createUser) {
        Optional<StreamingRoomPlaybackVideo> optional = streamingRoomPlaybackVideoRepository.findById(id);
        if(!optional.isPresent()){
            throw new BizException(ResponseCode.NOT_FIND_VIDEO);
        }
        StreamingRoomPlaybackVideo data = optional.get();
        data.initDel(createUser);
        streamingRoomPlaybackVideoRepository.save(data);

        String redisKey = String.format(
                RedisKey.LIVE_ROOM_PLAYBACK_VIDEOS, data.getRoomNo(),data.getVideoInfo().getFileId());
        redisUtils.remove(redisKey);
        log.info("删除回放视频 id={}。同时删除redis缓存，key={}",id,redisKey);
        return Boolean.TRUE;
    }

    /**
     * 查询房间10条回放视频
     */
    @Override
    public RoomPlaybackInfoVO listTop10(RoomPlaybackQueryParam param) {
        if(Objects.isNull(param) || StringUtils.isBlank(param.getRoomNo())){
            throw new BizException(ResponseCode.NO_ROOM_N0);
        }
        StreamingRoomVO roomInfo = streamingRoomServiceImpl.detail(param.getRoomNo());
        if(Objects.isNull(roomInfo)){
            throw new BizException(ResponseCode.AUCTION_ROOM_NULL);
        }
        List<RoomPlaybackVO> voList = new ArrayList<>();
        if(StringUtils.equals(roomInfo.getPlaybackSwitch(), SwitchEnum.OPEN.getValue()) &&
                !(roomInfo.getPlaybackTimeOut() < DateUtil.getTimeStrampSeconds() && roomInfo.getPlaybackTimeOut()>0)){
            List<StreamingRoomPlaybackVideo> dataList = streamingRoomPlaybackVideoRepository
                    .findTop10ByRoomNoAndDeletedAndPlaySortGreaterThanOrderByPlaySortAsc(param.getRoomNo(),Boolean.FALSE,param.getPlaySort());
            if(Objects.nonNull(dataList) && !dataList.isEmpty()){
                dataList.forEach(data -> voList.add(roomPlaybackVideoAsVO(data)));
            }
        }
        return RoomPlaybackInfoVO.builder().roomStatus(roomInfo.getStatus())
                .playbackSwitch(roomInfo.getPlaybackSwitch()).playbackTimeOut(roomInfo.getPlaybackTimeOut())
                .playbackVideos(voList).build();
    }


    /**
     * 根据文件id跟新回放视频封面;
     * @param fileId
     * @param cover
     */
    @Async
    @Override
    public void updatePlaybackCoverWithFileId(String fileId, String cover) {
        log.info("根据file id更新回放视频封面 fileId={}, cover={}",fileId,cover);
        Query query = Query.query(Criteria.where("videoInfo.fileId").is(fileId));
        Update update = new Update().set("videoInfo.cover", cover);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, StreamingRoomPlaybackVideo.class);
        log.info("根据file id更新回放视频封面,updateResult={}", updateResult);
    }


}
