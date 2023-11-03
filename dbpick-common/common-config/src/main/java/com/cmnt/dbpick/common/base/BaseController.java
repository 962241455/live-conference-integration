package com.cmnt.dbpick.common.base;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.enums.PlatformEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.jwt.JwtToken;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.model.HttpServerInfo;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import com.cmnt.dbpick.common.model.ThirdAccessPageInfo;
import com.cmnt.dbpick.common.utils.BeanMapUtils;
import com.cmnt.dbpick.common.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class BaseController<T> extends  BaseCommon {


    /**
     * 获取当前用户名
     *
     * @return
     */
    public String getUserId() {
        TokenInfo accessToken = getAccessToken();
        log.info("getUserId 获取到的 token 对象是： {}", accessToken);
        if (Objects.isNull(accessToken)) {
            return "";
        }
        return accessToken.getUserId();
    }

    /**
     * 获取默认房间号
     * @return
     */
    public String getRoomNo() {
        String liveRoomNo = request.getHeader("live_room_no");
        log.info("getRoomNo 获取默认房间号 对象是： {}", liveRoomNo);
        if (Objects.isNull(liveRoomNo)) {
            return "";
        }
        return liveRoomNo;
    }


    /**
     * 获取token
     * @return
     */
    public TokenInfo getAccessToken() {
        String token = request.getHeader("access_token");
        log.info("getAccessToken 获取到值为： {}", token);
        if (StrUtil.isEmpty(token)) {
            throw new BizException(ResponseCode.GET_TOKEN_ERROR);
        }
        TokenInfo userInfo = null;
        try {
            userInfo = BeanMapUtils.mapToBean(JwtToken.parseToken(token), TokenInfo.class);
        } catch (Exception e) {
            throw new BizException(ResponseCode.GET_TOKEN_ERROR);
        }
        if (Objects.isNull(userInfo)) {
            throw new BizException(ResponseCode.GET_TOKEN_ERROR);
        }
        return userInfo;
    }

    public String getThirdAccessToken() {
        String token = request.getHeader("access_token");
        log.info("getAccessToken 获取到值为： {}", token);
        TokenInfo userInfo = null;
        try {
            if (!StrUtil.isEmpty(token)) {
                userInfo = BeanMapUtils.mapToBean(JwtToken.parseToken(token), TokenInfo.class);
                log.info("getUserId 获取到的 token 对象是： {}", userInfo);
                return userInfo.getThirdId();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("获取到的 token 对象错误:{}",e.getMessage());
        }
        return "";
    }

    public void getParamAccess(ThirdAccessPageInfo info){
        if(StringUtils.isBlank(info.getThirdId())){
            TokenInfo accessToken = getAccessToken();
            info.setThirdId(accessToken.getThirdId());
            info.setCreateUser(accessToken.getUserId());
        }
        return ;
    }
    public void getParamAccess(ThirdAccessKeyInfo info){
        if(StringUtils.isBlank(info.getThirdId())){
            TokenInfo accessToken = getAccessToken();
            info.setThirdId(accessToken.getThirdId());
            info.setCreateUser(accessToken.getUserId());
        }
        return ;
    }

    /**
     * 获取设备信息
     */
    public PlatformEnum getPlatform() {
        String agentString = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(agentString);
        OperatingSystem operatingSystem = userAgent.getOperatingSystem(); // 操作系统信息
        eu.bitwalker.useragentutils.DeviceType deviceType = operatingSystem.getDeviceType(); // 设备类型

        switch (deviceType) {
            case COMPUTER:
                return PlatformEnum.Web;
            case TABLET:
            /*{
                if (agentString.contains("Android")) return "Android Pad";
                if (agentString.contains("iOS")) return "iPad";
                return "Unknown";
            }*/
            case MOBILE: {
                if (agentString.contains("Android")) return PlatformEnum.Android;
                if (agentString.contains("iOS")) return PlatformEnum.iOS;
                return PlatformEnum.OTHER;
            }
            default:
                return PlatformEnum.OTHER;
        }

    }
}
