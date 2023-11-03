package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.config.TencentConfig;
import com.cmnt.dbpick.common.enums.live.RoomStatusEnum;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.live.api.vo.LiveOpenVo;
import com.cmnt.dbpick.live.server.entity.tencent.StreamResponse;
import com.cmnt.dbpick.live.server.mongodb.document.LiveCount;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoom;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveCountRepository;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomRepository;
import com.cmnt.dbpick.live.server.service.TencentLiveService;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.enums.LiveListEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.tencentcloudapi.live.v20180801.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * tencent 直播模块
 *
 * @author mr . wei
 * @date 2022/7/25
 */
@Slf4j
@Service
public class TencentLiveServiceImpl implements TencentLiveService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TxCloudUtil txCloudUtil;

    @Autowired
    private StreamingRoomRepository streamingRoomRepository;
    @Autowired
    private LiveCountRepository liveCountRepository;




    @Override
    public LiveOpenVo liveOpen(String roomNo) {
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(roomNo);
        if (Objects.isNull(room)){
            log.error("liveOpen fail : because room = null ");
            throw new BizException(ResponseCode.AUCTION_ROOM_NULL);
        }
        return getSecretByRoomNo(room.getRoomNo());
    }

    @Override
    public LiveOpenVo getSecretByRoomNo(String roomNo){
        LiveOpenVo ret = new LiveOpenVo();
        ret.setPushUrl("rtmp://" + TencentConfig.TX_CLOUD_PUSH_URL + "/" + TencentConfig.TX_CLOUD_APPNAME + "/");
        String txSecret = txCloudUtil.getTxSecret(roomNo);
        log.info("生成串流密钥：{}", txSecret);
        ret.setSecret(roomNo + "?" + txSecret);
        ret.setPushUrlTimeOut(DateUtil.addDateSecond(DateUtil.fromDate2Str(new Date()),
                3600*TencentConfig.TX_PUSH_TIME_OUT.intValue()));
        ret.setPlayUrl("http://"+TencentConfig.TX_CLOUD_PLAYURL+ "/" + roomNo + ".flv");
        return ret;
    }


    @Override
    public boolean pushLiveStart(StreamResponse response) {

        log.info("腾讯云直播回调：启动回调 - 开始");
        log.info("response:" + response);
        String sign = DigestUtils.md5DigestAsHex(("AntLive" + response.getT()).getBytes());
        log.info("sign:" + sign + " ret:" + response.getSign().equals(sign));
        if (!response.getSign().equals(sign)){
            log.error("illegal request! stream_id:" + response.getStream_id());
            throw new BizException(ResponseCode.PRIVATE_KEY_NOT_EXIST);
        }
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(response.getStream_id());
        if (Objects.isNull(room)){
            log.error("live start fail : because room = null ");
            throw new BizException(ResponseCode.AUCTION_ILLEGAL_ROOM_NULL);
        }
        StreamingRoom streamingRoom = new StreamingRoom();
        room.setStatus(RoomStatusEnum.LIVE_ING.getValue());
        FastBeanUtils.copy(room,streamingRoom);
        streamingRoomRepository.save(streamingRoom);

        // record live duration
        LiveCount liveInfo = new LiveCount();
        liveInfo.setRoomNo(room.getRoomNo());
        liveInfo.setStatus(LiveListEnum.LiveInfoStatus.NO.getCode());
        liveInfo.setStartTime(ObjectTools.GetCurrentTime());
        liveInfo.initSave("");
        liveCountRepository.save(liveInfo);

        //开始统计直播数据
        String key = String.format(RedisKey.LIVE_KEY_PREFIX, streamingRoom.getId());
        Map<Object,Object> map = new HashMap<>(2);
        map.put("live_info_id",liveInfo.getId());
        map.put("room_id",streamingRoom.getId());
        log.info("直播开始：Redis开始统计直播数据 - key:{}， live_info_id:{},room_id:{}",key,liveInfo.getId(),streamingRoom.getId());
        redisUtils.addHashMap(key,map);
        log.info("直播开始：Redis开始统计直播数据 - 插入完成");
        log.info("腾讯云直播回调：启动回调 - 结束");
        return  true;
    }

    @Override
    public boolean pushLiveEnd(StreamResponse response) {
        log.info("腾讯云直播回调：结束回调 - 开始");
        String sign = DigestUtils.md5DigestAsHex(("AntLive" + response.getT()).getBytes());
        log.info("sign:" + sign + " ret:" + response.getSign().equals(sign));
        if (!response.getSign().equals(sign)){
            throw new BizException(ResponseCode.PRIVATE_KEY_NOT_EXIST);
        }

        // update room status
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(response.getStream_id());
        if (Objects.isNull(room)){
            log.error("live start fail : because room = null ");
            throw new BizException(ResponseCode.AUCTION_ILLEGAL_ROOM_NULL);
        }
        room.setStatus(RoomStatusEnum.NO_LIVE.getValue());
        streamingRoomRepository.save(room);

        // update live info, add end time
        LiveCount liveCount = liveCountRepository.findTopByRoomNoOrderByCreateDateTimeDesc(room.getRoomNo());
        if (liveCount.getEndTime()!=null){
            throw new BizException(ResponseCode.LIVE_ROOM_END);
        }
        // 不能直接更新liveInfo
        LiveCount updateInfo = new LiveCount();
        updateInfo.setId(liveCount.getId());
        updateInfo.setEndTime(LocalDateTime.now());
        // 0-living 1-finished
        updateInfo.setStatus(LiveListEnum.LiveInfoStatus.YES.getCode());

        //结束统计数据
        String key = String.format(RedisKey.LIVE_KEY_PREFIX, String.valueOf(room.getId()));
        try {

            Map<Object, Object> map = redisUtils.hmGetAll(key);
            log.info("直播结束：Redis获取统计数据 map size:{}，key:{}",map.size(),key);
            String pc = String.valueOf(map.getOrDefault(RedisKey.LIVE_PRESENT_COUNT,"0"));
            String cc = String.valueOf(map.getOrDefault(RedisKey.LIVE_CLICK_COUNT,"0"));
            String dm = String.valueOf(map.getOrDefault(RedisKey.LIVE_DAN_MU_COUNT,"0"));

            updateInfo.setClickCount(cc);
            updateInfo.setPresentCount(pc);
            updateInfo.setDanMuCount(dm);

        }catch (Exception e){
            log.error("直播结束 : Redis取值出现异常 {}",e.getMessage());
            e.printStackTrace();
        }finally {
            redisUtils.remove(key);
        }

        liveCountRepository.save(updateInfo);
        log.info("腾讯云直播回调：结束回调 - 结束");
        return true;
    }




    @Override
    public boolean ban(Integer rid, String resumeTime, String reason) {
//
//        List<BanRecord> banRecords = banRecordMapper.selectList(new QueryWrapper<BanRecord>().eq("room_id", rid).eq("status", 0));
//        if (banRecords.size() > 0) {
//            return false;
//        }
//
//        LiveInfo liveInfo = liveInfoMapper.selectOne(new QueryWrapper<LiveInfo>().eq("room_id", rid).orderByDesc("id").last("limit 0,1"));
//        if (liveInfo.getEndTime() == null) {
//            // 不能直接更新liveInfo
//            LiveInfo updateInfo = new LiveInfo();
//            updateInfo.setId(liveInfo.getId());
//            updateInfo.setEndTime(LocalDateTime.now());
//            // 0-living 1-finished
//            updateInfo.setStatus(Constants.LiveInfoStatus.YES.getCode());
//            liveInfoMapper.updateById(updateInfo);
//        }
//
//        try {
//            roomMapper.updateById(Room.builder().id(rid).status(Constants.LiveStatus.BANNING.getCode()).build());
//        } catch (Exception e) {
//            log.error("更新房间状态异常:{}", e.getMessage());
//        }

        log.info("调用腾讯云封禁接口: rid = " + rid);
        ForbidLiveStreamRequest forbidLiveStreamRequest = new ForbidLiveStreamRequest();
        forbidLiveStreamRequest.setAppName("live");
        forbidLiveStreamRequest.setDomainName("live.imhtb.cn");
        forbidLiveStreamRequest.setStreamName(String.valueOf(rid));
        forbidLiveStreamRequest.setReason("reason");


//        LocalDateTime now = LocalDateTime.now();
//        BanRecord record = new BanRecord();
//        record.setRoomId(rid);
//        record.setReason(reason);
//        record.setCreateTime(now);
//        record.setStartTime(now);
//        record.setResumeTime(resumeLocalDateTime);
//        record.setStatus(0);
//        banRecordMapper.insert(record);
//
//        roomMapper.updateById(Room.builder().id(rid).status(Constants.LiveStatus.BANNING.getCode()).build());

        log.info("调用腾讯云封禁接口：执行成功");
        return true;
    }

    @Override
    public boolean resume(Integer rid) {

//
//        BanRecord record = banRecordMapper.selectOne(new QueryWrapper<BanRecord>().eq("room_id", rid).eq("status", 0).orderByDesc("id").last("limit 0,1"));
//        if (record != null) {
//            BanRecord update = new BanRecord();
//            update.setId(record.getId());
//            update.setMark("手动恢复");
//            update.setStatus(1);
//            banRecordMapper.updateById(update);
//        }
//
//        roomMapper.updateById(Room.builder().id(rid).status(Constants.LiveStatus.STOP.getCode()).build());

        log.info("调用腾讯云恢复接口：执行结束");
        return true;
    }




}
