package com.cmnt.dbpick.stats.server.utils;

import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.enums.live.MessageStatusEnum;
import com.cmnt.dbpick.common.enums.live.MessageTypeEnum;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.tx.tencent.TxCloudImUtil;
import com.cmnt.dbpick.common.tx.tencent.request.im.ImNoticeMessageParam;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.live.api.model.StreamingRoomInfo;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.live.api.vo.RoomHotOnlineVO;
import com.cmnt.dbpick.stats.server.mongodb.document.LiveVote;
import com.cmnt.dbpick.stats.server.mongodb.document.StreamingRoom;
import com.cmnt.dbpick.stats.server.mongodb.repository.LiveVoteRepository;
import com.cmnt.dbpick.stats.server.mongodb.repository.StreamingRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户信息缓存redis工具类
 */
@Slf4j
@Component
public class StreamingRoomUtil {

    @Autowired
    private MongoPageHelper mongoPageHelper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TxCloudImUtil txCloudImUtil;
    @Autowired
    private LiveRoomRedisUtil liveRoomRedisUtil;

    @Autowired
    private StreamingRoomRepository streamingRoomRepository;
    @Autowired
    private LiveVoteRepository liveVoteRepository;

    /**
     * 查询用户所有直播房间号
     */
    public List<String> getRoomNosByThird(ThirdRoomQueryParam param){
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

    public StreamingRoomVO getByRoomNoAndRefreshRedis(String roomNo) {
        StreamingRoomVO roomVO = liveRoomRedisUtil.getInfoByRoomNo(roomNo);
        if (Objects.isNull(roomVO)) {
            StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo);
            if (Objects.nonNull(room)) {
                roomVO = refreshRoomAsVO(room);
            }
        }
        return roomVO;
    }
    private StreamingRoomVO refreshRoomAsVO(StreamingRoom room){
        log.info("刷新直播房间信息 room={}", room);
        StreamingRoomVO roomVO = new StreamingRoomVO();
        FastBeanUtils.copy(room.getInfo(), roomVO);
        roomVO.setId(room.getId());
        roomVO.setThirdId(room.getThirdId());
        roomVO.setRoomNo(room.getRoomNo());
        roomVO.setStatus(room.getStatus());
        roomVO.setClassify(room.getClassify());
        roomVO.setType(room.getType());
        roomVO.setWatchFilter(room.getWatchFilter());
        roomVO.setVisitorSwitch(room.getVisitorSwitch());
        roomVO.setPlaybackSwitch(room.getPlaybackSwitch());
        roomVO.setPlaybackTimeOut(room.getPlaybackTimeOut());
        roomVO.setCreateDateTime(room.getCreateDateTime());
        liveRoomRedisUtil.setRoomInfo(roomVO);
        return roomVO;
    }

    /**
     * 更新直播间热度和在线人数
     */
    public Boolean updRoomHotAndOnline(String roomNo,Integer hotIncr,Integer onlineIncr){
        log.info("更新直播间热度和在线人数:roomNo={}，hotIncr={}，onlineIncr={}",roomNo,hotIncr,onlineIncr);
        if(StringUtils.isBlank(roomNo)){
            return Boolean.TRUE;
        }
        String realRoomNo = RoomCommonUtil.getRoomWithPlayback(roomNo);
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(realRoomNo);
        if (Objects.nonNull(room)){
            StreamingRoomInfo info = room.getInfo();
            Integer nowHotScores = info.getHotScores()+hotIncr;
            Integer nowOnline = info.getOnline()+onlineIncr;
            nowOnline = nowOnline>0?nowOnline:0;
            mongoTemplate.findAndModify(
                    new Query(Criteria.where("roomNo").is(realRoomNo)),
                    new Update().set("lastModifiedDate", ObjectTools.GetCurrentTime()).inc("info.hotScores", hotIncr)
                            .inc("info.online", nowOnline>0?onlineIncr:0), StreamingRoom.class);
            info.setHotScores(nowHotScores);
            info.setOnline(nowOnline);
            log.info("更新redis直播间热度和在线人数，info={}",info);
            room.setInfo(info);
            refreshRoomAsVO(room);
            sendImHotMsg(roomNo);
            /** 更新用户热度值 */
            String redisKey = String.format(RedisKey.LIVE_USER_HOT_ONLINE, room.getCreateUser());
            redisUtils.incrBy(redisKey, 1L);
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
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(
                RoomCommonUtil.getRoomWithPlayback(roomNo));
        if(Objects.isNull(room)){
            log.info("发送mq并同步热度值消息：mongo room info is null");
            return;
        }
        Integer hotScores = Objects.isNull(room.getInfo().getHotScores()) ?0:room.getInfo().getHotScores();
        Integer online = Objects.isNull(room.getInfo().getOnline())?0:room.getInfo().getOnline();
        RoomHotOnlineVO vo = RoomHotOnlineVO.builder().roomNo(roomNo).online(online).hotScores(hotScores).build();
        ImNoticeMessageParam msg = ImNoticeMessageParam.builder().groupId(vo.getRoomNo())
                .msgKey(vo.getRoomNo()).msgContent(vo).msgType(MessageTypeEnum.HOT_ONLINE.getValue()).build();
        log.info("准备发送im热度消息，msg={}",msg);
        txCloudImUtil.imGroupSystemPush(msg);
    }


    /**
     * 根据房间号停止所有投票信息
     */
    public Boolean stopVoteByRoomNo(String roomNo, String createBy){
        log.info("根据房间号停止所有投票信息,roomNo={}",roomNo);
        LiveVote voteParam = LiveVote.builder().roomNo(roomNo).build();
        voteParam.setDeleted(Boolean.FALSE);
        List<LiveVote> voteList = liveVoteRepository.findAll(Example.of(voteParam));
        if(Objects.isNull(voteList) || voteList.isEmpty()){
            return Boolean.TRUE;
        }
        voteList.forEach(
                vote -> {
                    vote.setStatus(MessageStatusEnum.STOP_PUBLISH.getValue());
                    vote.initUpdate(createBy);
                }
        );
        log.info("更新的投票信息 voteList={}", voteList);
        liveVoteRepository.saveAll(voteList);
        return Boolean.TRUE;
    }

}
