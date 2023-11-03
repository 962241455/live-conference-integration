package com.cmnt.dbpick.common.utils;

import com.cmnt.dbpick.common.config.TencentConfig;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.tencentyun.TLSSigAPIv2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Slf4j
@Component
@DependsOn("tencentConfig")
public class TxCloudUtil implements InitializingBean {

    @Autowired
    private RedisUtils redisUtils;

    private TLSSigAPIv2 tlsSigApi;

    /**
     * @param uid
     * 获取腾讯云用户签名
     */
    public String getTxCloudUserSig(String uid) {
        String redisKey = String.format(RedisKey.REDIS_IM_USER_SIG, uid);
        String userSig = (String) redisUtils.get(redisKey);
        if (StringUtils.isEmpty(userSig)) {
            //TLSSigAPIv2 tlsSigApi = new TLSSigAPIv2(TencentConfig.TX_CLOUD_SDK_APPID, TencentConfig.TX_CLOUD_KEY);
            userSig = tlsSigApi.genUserSig(uid, 86400*15);
            redisUtils.set(redisKey, userSig, 86400L*15);
        }
        return userSig;
    }


    /**
     * 获取推流密钥
     * @param streamName
     * @return
     */
    public String getTxSecret(String streamName) {
        LocalDateTime plusTimeOut = LocalDateTime.now().plusHours(TencentConfig.TX_PUSH_TIME_OUT);
        log.info("获取推流密钥 plusTimeOut={}",plusTimeOut);
        long txTime = plusTimeOut.toEpochSecond(ZoneOffset.of("+8"));
        String input = TencentConfig.TX_PUSH_KEY + streamName +  Long.toHexString(txTime).toUpperCase();
        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret = StringFormat.byteArrayToHexString( messageDigest.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return txSecret == null ? "" : "txSecret="+txSecret+"&txTime=" + Long.toHexString(txTime).toUpperCase();
    }


    /**
     * 房间断流之后获取 直播间状态变更 处理时间
     */
    public Long getLiveStopHandleTime() {
        Object handleTime = redisUtils.get(RedisKey.STOP_STREAM_HANDLE_TIME);
        if (Objects.isNull(handleTime)) {
            redisUtils.set(RedisKey.STOP_STREAM_HANDLE_TIME, 10);
            handleTime = 10;
        }
        Integer handleTimeMinute = (int)handleTime;
        return DateUtil.getTimeStrampSeconds()+handleTimeMinute*60*1000;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        tlsSigApi = new TLSSigAPIv2(TencentConfig.TX_CLOUD_SDK_APPID, TencentConfig.TX_CLOUD_KEY);
    }
}
