package com.cmnt.dbpick.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.constant.Constants;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.user.RoomConfigVO;
import com.cmnt.dbpick.common.user.ThirdAccessKeyVO;
import com.cmnt.dbpick.common.user.ThirdSysUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 商户账号和访问凭证工具类
 */
@Slf4j
@Component
public class AccessAuthUtil {

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 缓存 redis 商户账号信息(包含ak sk)
     * @param thirdSysUserVO
     */
    public void setSysThirdUserInfo(ThirdSysUserVO thirdSysUserVO) {
        String redisKey = String.format(RedisKey.SYS_THIRD_USER, thirdSysUserVO.getThirdId());
        log.info("商户账号信息缓存redis: key={}",redisKey);
        redisUtils.set(redisKey, JSON.toJSONString(thirdSysUserVO));
    }

    /**
     * 获取redis 商户账号信息
     * @param thirdId
     */
    public ThirdSysUserVO getSysThirdUserInfo(String thirdId) {
        String redisKey = String.format(RedisKey.SYS_THIRD_USER,thirdId);
        Object object = redisUtils.get(redisKey);
        ThirdSysUserVO thirdSysUserVO = null;
        if (Objects.nonNull(object)) {
            thirdSysUserVO = JSONObject.toJavaObject(JSONObject.parseObject(object.toString()), ThirdSysUserVO.class);
            if (thirdSysUserVO.getDeleted()) {
                throw new BizException(ResponseCode.THIRD_USER_NOT_EXIST);
            }
        }
        return thirdSysUserVO;
    }

    /**
     * 移除redis 商户账号信息(包含ak sk)
     * @param thirdId
     * @return
     */
    public void delSysThirdUserInfo(String thirdId) {
        String redisKey = String.format(RedisKey.SYS_THIRD_USER, thirdId);
        log.info("移除redis 商户账号信息: key={}",redisKey);
        redisUtils.remove(redisKey);
    }



    /**
     * 缓存 redis 商户凭证信息
     * @param vo
     */
    public void setThirdAccessKeyInfo(ThirdAccessKeyVO vo) {
        String redisKey = String.format(RedisKey.SYS_THIRD_USER_ACCESS, vo.getThirdId());
        log.info("商户凭证信息缓存redis: key={}",redisKey);
        redisUtils.set(redisKey, JSON.toJSONString(vo));
    }
    /**
     * 获取 redis 商户凭证信息
     * @param thirdId
     */
    public ThirdAccessKeyVO getThirdAccessKeyInfo(String thirdId) {
        String redisKey = String.format(RedisKey.SYS_THIRD_USER_ACCESS,thirdId);
        Object object = redisUtils.get(redisKey);
        log.info("获取 redis 商户凭证信息： redisKey={}, object={}",redisKey,object);
        ThirdAccessKeyVO thirdAccessKeyVO = null;
        if (Objects.nonNull(object)) {
            thirdAccessKeyVO = JSONObject.toJavaObject(JSONObject.parseObject(object.toString()), ThirdAccessKeyVO.class);
            if (thirdAccessKeyVO.getDeleted()) {
                throw new BizException(ResponseCode.NOT_FIND_ACCESS);
            }
        }
        log.info("获取 redis 商户凭证信息： thirdAccessKeyVO={}",thirdAccessKeyVO);
        return thirdAccessKeyVO;
    }
    public String getThirdAK(String thirdId) {
        ThirdAccessKeyVO info = getThirdAccessKeyInfo(thirdId);
        if(Objects.isNull(info) || StringUtils.isBlank(info.getAccessKeyId())){
            throw new BizException(ResponseCode.NOT_FIND_ACCESS);
        }
        return info.getAccessKeyId();
    }

    public void delThirdAccessKeyInfo(String thirdId) {
        String redisKey = String.format(RedisKey.SYS_THIRD_USER_ACCESS,thirdId);
        log.info("移除redis 商户账号信息: key={}",redisKey);
        redisUtils.remove(redisKey);
    }





    /**
     * 缓存 redis 直播间默认配置
     */
    public void setRoomConfigInfo(RoomConfigVO configVo, String thirdId) {
        String redisKey = String.format(RedisKey.THIRD_ROOM_CONFIG, thirdId);
        log.info("直播间默认配置信息缓存redis: key={}",redisKey);
        redisUtils.set(redisKey, JSON.toJSONString(configVo));
    }
    /**
     * 获取 redis 直播间默认配置
     * @param thirdId
     */
    public RoomConfigVO getRoomConfigInfo(String thirdId) {
        String redisKey = String.format(RedisKey.THIRD_ROOM_CONFIG, thirdId);
        Object object = redisUtils.get(redisKey);
        log.info("获取 redis 直播间默认配置: key={},Object={}",redisKey,object);
        if (Objects.isNull(object)) {
            return RoomConfigVO.builder()
                    .title("欢迎来到直播间")
                    .bgImg(Constants.CMNT_DEFAULT_LOG)
                    .logoCover(Constants.CMNT_DEFAULT_LOG)
                    .streamerAvatar(Constants.getDefaultImgae()).build();
        }
        return JSONObject.toJavaObject(JSONObject.parseObject(object.toString()), RoomConfigVO.class);
    }


}
