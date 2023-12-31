package com.cmnt.dbpick.manager.server.security.service;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.IPUtils;
import com.cmnt.dbpick.manager.server.entity.LoginUserDetails;
import com.cmnt.dbpick.manager.server.common.Constants;
import com.cmnt.dbpick.manager.server.util.ServletUtils;
import com.cmnt.dbpick.common.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * token验证处理
 *
 *  *
 */
@Component
public class TokenService
{
    // 令牌自定义标识
    //@Value("${token.header}")
    private String header = "header";

    // 令牌秘钥
    //@Value("${token.secret}")
    private String secret = "secret";

    // 令牌有效期（默认30分钟）
    //@Value("${token.expireTime}")
    private int expireTime = 20000;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUserDetails getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StrUtil.isEmpty(token)){
            return null;
        }
        Claims claims = parseToken(token);
        // 解析对应的权限以及用户信息
        String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
        String userKey = getTokenKey(uuid);
        LoginUserDetails user = JSONObject.toJavaObject(JSONObject.parseObject(redisUtils.get(userKey).toString()), LoginUserDetails.class);
        return user;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUserDetails loginUserDetails)
    {
        if (ObjectUtil.isNotNull(loginUserDetails) && StrUtil.isNotEmpty(loginUserDetails.getToken()))
        {
            String userKey = getTokenKey(loginUserDetails.getToken());
            redisUtils.set(userKey, JSON.toJSONString(loginUserDetails));
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token)
    {
        if (StrUtil.isNotEmpty(token))
        {
            String userKey = getTokenKey(token);
            redisUtils.remove(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUserDetails 用户信息
     * @return 令牌
     */
    public String createToken(LoginUserDetails loginUserDetails)
    {
        String token = IdUtil.fastUUID();
        loginUserDetails.setToken(token);
        setUserAgent(loginUserDetails);
        refreshToken(loginUserDetails);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @return 令牌
     */
    public void verifyToken(LoginUserDetails loginUserDetails)
    {
        long expireTime = loginUserDetails.getExpireTime();
        long currentTime = DateUtil.getTimeStrampSeconds();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUserDetails);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUserDetails 登录信息
     */
    public void refreshToken(LoginUserDetails loginUserDetails)
    {
        loginUserDetails.setLoginTime(DateUtil.getTimeStrampSeconds());
        loginUserDetails.setExpireTime(loginUserDetails.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUserDetails.getToken());
        redisUtils.set(userKey, JSON.toJSONString(loginUserDetails));
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUserDetails 登录信息
     */
    public void setUserAgent(LoginUserDetails loginUserDetails)
    {
        UserAgent userAgent = UserAgentUtil.parse(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IPUtils.getIpAddr(ServletUtils.getRequest());
        loginUserDetails.setIpaddr(ip);
        //loginUserDetails.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUserDetails.setBrowser(userAgent.getBrowser().getName());
        loginUserDetails.setOs(userAgent.getOs().getName());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if(StrUtil.isBlank(token)){
            token = request.getParameter(header);
        }
        if (StrUtil.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid)
    {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }
}
