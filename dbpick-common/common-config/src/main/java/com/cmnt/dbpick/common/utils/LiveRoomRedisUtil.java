package com.cmnt.dbpick.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
/**
 * 直播间工具类
 */
@Slf4j
@Component
public class LiveRoomRedisUtil {

    public static final String VISITOR_NAME = "%s观众%s";

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 获取 redis 房间信息
     * @param roomNo
     * @return
     */
    public StreamingRoomVO getInfoByRoomNo(String roomNo) {
        Object hashVal = redisUtils.getHash(RedisKey.STREAMING_ROOM, roomNo);
        if (Objects.isNull(hashVal)) {
            return null;
        }
        return JSONObject.toJavaObject(JSONObject.parseObject(hashVal.toString()), StreamingRoomVO.class);
    }
    public void setRoomInfo(StreamingRoomVO streamingRoom) {
        if (Objects.isNull(streamingRoom)) {
            return;
        }
        redisUtils.hmSet(RedisKey.STREAMING_ROOM, streamingRoom.getRoomNo(), JSON.toJSONString(streamingRoom));
    }

    /**
     * 获取下一个房间号
     * @return
     */
    public Long incRoomNo() {
        return redisUtils.incrBy(RedisKey.STREAMING_ROOM_NO, 1);
    }



    /**
     * 获取房间观众编号
     * @param roomNo
     */
    public String getRoomNextVisitorNo(String roomNo) {
        String redisKey = String.format(RedisKey.STREAMING_ROOM_VISITOR_NO, roomNo);
        Long aLong = redisUtils.incrBy(redisKey, 1);
        return String.format(VISITOR_NAME, roomNo, aLong);
    }
}
