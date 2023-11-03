package com.cmnt.dbpick.stats.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.enums.live.RoomStatusEnum;
import com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum;
import com.cmnt.dbpick.common.enums.tencent.TxUserActType;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.page.PageUtils;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.user.UserBaseInfo;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.stats.api.params.QueryRoomStatsParams;
import com.cmnt.dbpick.stats.api.vo.LiveUserRecordVO;
import com.cmnt.dbpick.stats.api.params.RoomUserRecordParam;
import com.cmnt.dbpick.stats.api.vo.LiveUserStatsVO;
import com.cmnt.dbpick.stats.server.es.ESUtils;
import com.cmnt.dbpick.stats.server.es.document.RoomUserRecordIndex;
import com.cmnt.dbpick.stats.server.es.repository.RoomUserRecordEsRepository;
import com.cmnt.dbpick.stats.server.service.RoomUserRecordService;
import com.cmnt.dbpick.stats.server.utils.StreamingRoomUtil;
import com.cmnt.dbpick.user.api.feign.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class RoomUserRecordServiceImpl implements RoomUserRecordService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ESUtils esUtils;
    @Autowired
    private UserClient userClient;

    @Autowired
    private StreamingRoomUtil streamingRoomUtil;

    @Autowired
    private RoomUserRecordEsRepository roomUserRecordEsRepository;
//    @Autowired
//    private RoomUserStatsEsRepository roomUserStatsEsRepository;


    @Override
    public List<RoomUserRecordIndex> saveRoomUserRecord(RoomUserRecordParam param) {
        log.info("保存im直播间用户记录, param={}", JSON.toJSONString(param));
        List<RoomUserRecordIndex> voList = new ArrayList<>();
        List<String> memberIdList = param.getMemberIdList();
        memberIdList.forEach(memberId->{
            RoomUserRecordIndex record = initUserRecord(param, memberId);
            if(Objects.nonNull(record)){
                voList.add(record);
            }
        });
        roomUserRecordEsRepository.saveAll(voList);
        log.info("保存im直播间用户记录到es完成, dataJsons={}", voList);
        return voList;
    }

    /**
     * 组装用户登录记录
     */
    private RoomUserRecordIndex initUserRecord(RoomUserRecordParam param, String memberId){
        String[] strs = UserInfoUtils.splitUserRoomRole(memberId);
        String userId = strs[0];
        String groupId = StringUtils.isBlank(param.getGroupId())?strs[1]:param.getGroupId();
        String roomNo = groupId.contains(RoomCommonUtil.PLAYBACK_ROOM_ID)?
                RoomCommonUtil.getRoomWithPlayback(groupId):groupId;
        String playbackRoomNo = RoomCommonUtil.getPlaybackRoom(roomNo);

        StreamingRoomVO vo = streamingRoomUtil.getByRoomNoAndRefreshRedis(roomNo);
        String thirdId = "";
        String thirdUserId = "";
        String userRole = "";
        String currentRoomNo = groupId;
//        String currentWatchType = WatchRoomTypeEnum.LIVE.getValue();
        if (Objects.nonNull(vo) && StringUtils.isNotBlank(vo.getThirdId())){
            thirdId = vo.getThirdId();
            if(!StringUtils.equals(vo.getStatus(), RoomStatusEnum.LIVE_ING.getValue())
                    && !groupId.contains("playback")){
                log.info("组装im直播间用户记录信息：直播间={}此时状态为非直播中={}，保存es记录的房间号",
                        roomNo,vo.getStatus());
                currentRoomNo = playbackRoomNo;
            }
        }
        ResponsePacket<UserBaseInfo> result = userClient.findUserBaseInfo(userId);
        if (Objects.nonNull(result) && Objects.nonNull(result.getData())){
            UserBaseInfo userInfo = result.getData();
            thirdUserId = userInfo.getThirdUserId();
            userRole = userInfo.getUserRole();
        }
        String redisKey = String.format(RedisKey.USER_ROOM_ROLE_ACT_TM,
                userId+"_"+currentRoomNo+"_"+userRole+"_"+param.getActType());
        Object aLong = redisUtils.incrBy(redisKey, 1L);
        if(Integer.parseInt(String.valueOf(aLong))>1){
            log.info("3s内已有重复{}记录：key={},value={}",
                    param.getActType(),redisKey, aLong);
            return null;
        }
        Long eventMillis = param.getEventTime();
        String nowDateStr = DateUtil.fromDate2Str(new Date(eventMillis));
        String clientIP = param.getClientIP();
        RoomUserRecordIndex recordIndex = RoomUserRecordIndex.builder().userId(userId).thirdId(thirdId).roomNo(currentRoomNo)
                .watchType(currentRoomNo.contains("playback")?WatchRoomTypeEnum.PLAYBACK.getValue():WatchRoomTypeEnum.LIVE.getValue())
                .actType(param.getActType()).actTimeMillis(eventMillis).actTime(nowDateStr).terminalType(StringUtils.lowerCase(param.getOptPlatform()))
                .ip(clientIP).area(IpAreaUtils.getAreaInfo(clientIP)).thirdUserId(thirdUserId).userRole(userRole).build();
        String esId = userId+"_"+currentRoomNo+"_"+eventMillis;
        recordIndex.setId(esId);
        log.info("保存im直播间用户记录到es, esId={}", esId);
        redisUtils.expire(redisKey,3L);
        return recordIndex;
    }

    /**
     * 统计直播间热度和在线人数
     */
    @Async
    @Override
    public void handleRoomHotAndOnline(List<RoomUserRecordIndex> recordList) {
        log.info("统计直播间热度和在线人数 recordList={}",recordList);
        if(Objects.isNull(recordList) || recordList.isEmpty()){
            return;
        }
        recordList.forEach(
                record -> {
                    boolean isEnterStatus = StringUtils.equals(
                            record.getActType(), TxUserActType.ENTER_ROOM.getValue());
                    streamingRoomUtil.updRoomHotAndOnline(record.getRoomNo(),isEnterStatus?1:0,isEnterStatus?1:-1);
                }
        );
    }

    /**
     * 查询用户观看记录列表
     * @param param
     * @return
     */
    @Override
    public PageResponse<LiveUserRecordVO> userRecord(QueryRoomStatsParams param) {
        if(Objects.isNull(param) || StringUtils.isBlank(param.getRoomNo())){
            throw new BizException(ResponseCode.NO_ROOM_N0);
        }
        BoolQueryBuilder queryParam = QueryBuilders.boolQuery();
        queryParam.must(QueryBuilders.wildcardQuery("roomNo", (param.getRoomNo()+"*")));
        if (StringUtils.isNotBlank(param.getUserId())){
            queryParam.must(QueryBuilders.termQuery("userId",param.getUserId()));
        }
        if (StringUtils.isNotBlank(param.getThirdUserId())){
            queryParam.must(QueryBuilders.termQuery("thirdUserId",param.getThirdUserId()));
        }
        if (StringUtils.isNotBlank(param.getWatchType())){
            queryParam.must(QueryBuilders.termQuery("watchType",param.getWatchType()));
        }
        Pageable page = PageRequest.of((param.getPageNum()-1),param.getPageSize(),
                Sort.Direction.DESC,"actTimeMillis");
        Page<RoomUserRecordIndex> data = roomUserRecordEsRepository.search(queryParam, page);
        PageResponse<RoomUserRecordIndex> response = PageUtils.handlePageToResponse(data);
        response.setPageNum(param.getPageNum());
        response.setPageSize(param.getPageSize());
        return JacksonUtils.toBean(JacksonUtils.toJson(response), PageResponse.class);
    }

//    /**
//     *  异步保存不同数据到不同房间es
//     */
//    @Override
//    @Async
//    public Boolean saveRecordListByRoomNo(List<RoomUserRecordIndex> recordList) {
//        log.info("异步保存用户记录到es(根据房间号区分)，recordList={}",recordList);
//        if(Objects.isNull(recordList) || recordList.isEmpty()){
//            return Boolean.TRUE;
//        }
//        String esIndexRule = getRoomUserRecordEsIndexRule();
//        log.info("不同房间用户记录索引命名规则: esIndexRule={}", esIndexRule);
//        Map<String, List<RoomUserRecordIndex>> recordListMap =
//                recordList.stream().collect(Collectors.groupingBy(RoomUserRecordIndex::getRoomNo));
//        recordListMap.forEach((k,v)->{
//            String esIndexName = String.format(esIndexRule, k.replace("_playback",""));
//            List<String> batchId = v.stream().map(RoomUserRecordIndex::getId).collect(Collectors.toList());
//            List<String> batchRecordJson = v.stream().map(vo->JSON.toJSONString(vo)).collect(Collectors.toList());
//            esUtils.putBatch(esIndexName, batchRecordJson, batchId);
//        });
//
//        return Boolean.TRUE;
//    }
//    public String getRoomUserRecordEsIndexRule() {
//        String redisKey = RedisKey.ES_ROOM_INDEX_NAME_RULE;
//        Object object = redisUtils.get(redisKey);
//        log.info("获取 redis 不同房间用户记录 索引命名规则: key={},Object={}", redisKey, object);
//        if (Objects.isNull(object)) {
//            String esIndexRule = RedisKey.ES_ROOM_INDEX_USER_RECORD_DEFAULT;
//            redisUtils.set(redisKey, esIndexRule);
//            return esIndexRule;
//        }
//        return String.valueOf(object);
//    }



    /**
     * 查询用户观看数据统计列表
     * @param param
     * @return
     */
    @Override
    public PageResponse<LiveUserStatsVO> userStats(QueryRoomStatsParams param) {
        /*BoolQueryBuilder queryParam = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(param.getRoomNo())){
            queryParam.must(QueryBuilders.termQuery("roomNo",param.getRoomNo()));
        }
        if (StringUtils.isNotBlank(param.getThirdUserId())){
            queryParam.must(QueryBuilders.termQuery("thirdUserId",param.getThirdUserId()));
        }
        if (StringUtils.isNotBlank(param.getUserId())){
            queryParam.must(QueryBuilders.termQuery("userId",param.getUserId()));
        }
        Pageable page = PageRequest.of((param.getPageNum()-1),param.getPageSize(),
                Sort.Direction.DESC,"actTimeMillis");
        Page<RoomUserStatsIndex> data = roomUserStatsEsRepository.search(queryParam, page);
        PageResponse<RoomUserStatsIndex> response = PageUtils.handlePageToResponse(data);
        response.setPageNum(param.getPageNum());
        response.setPageSize(param.getPageSize());
        return JacksonUtils.toBean(JacksonUtils.toJson(response), PageResponse.class);*/
        return new PageResponse<LiveUserStatsVO>();
    }

}
