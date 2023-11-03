package com.cmnt.dbpick.gateway.server.filter;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.ObjectTools;
import com.cmnt.dbpick.common.utils.RedisUtils;
import com.cmnt.dbpick.gateway.server.config.IgnoreUrlsConfig;
import com.cmnt.dbpick.gateway.server.config.WhitelistConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


@Slf4j
@Component
public class GatewayFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";

    /*@Resource
    private CustomTokenStore tokenStore;

    @Resource
    ResourceServerTokenServices tokenService;*/
    public static final String USER_TOKEN = "user_token:%s:sys_code_%s";

    RedisUtils  redisUtils;

    @Resource
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Resource
    private WhitelistConfig whitelistConfig;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = DateUtil.getTimeStrampSeconds();
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求方法uri={},startTime={}",request.getURI(),startTime);
        String requestUrl = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        //以下服务直接放行
        /*if (requestUrl.contains("/user/") || requestUrl.contains("/live/")){
            log.info("服务直接放行......");
            return getVoidMono(exchange, chain, startTime);
        }*/
        // 白名单路径直接放行
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, requestUrl)) {
                // 获取token
                String token = getToken(exchange);
                if (StringUtils.isEmpty(token)) {
                    return getVoidMono(exchange, chain,startTime);
                } else {
                    return checkToken(exchange, chain, token,startTime);
                }
            }
        }
        // 2 检查token是否存在
        String token = getToken(exchange);
        if (StringUtils.isEmpty(token)) {
            return noTokenMono(exchange);
        }
        return checkToken(exchange, chain, token,startTime);
    }

    private Mono<Void> getVoidMono(ServerWebExchange exchange, GatewayFilterChain chain,long startTime) {
        return chain.filter(exchange).then( Mono.fromRunnable(() -> {
            ServerHttpRequest request = exchange.getRequest();
            Long executeTime = (DateUtil.getTimeStrampSeconds() - startTime);
            log.info("请求方法 method={},uri={},executeTime={},params={},address={},headers={}", request.getMethodValue(),
                    request.getURI(),executeTime + "ms", request.getQueryParams(), request.getRemoteAddress(),
                    exchange.getRequest().getHeaders().getFirst("Authorization"));
        }));
    }

    /**
     * 获取token
     */
    private String getToken(ServerWebExchange exchange) {
        String tokenStr = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isEmpty(tokenStr)) {
            return null;
        }
        String token = tokenStr.split(" ")[1];
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return token;
    }

    /**
     * 校验token
     */
    public Mono<Void> checkToken(ServerWebExchange exchange, GatewayFilterChain chain, String token,long starTime)
            throws IOException {

        try {
            // 取出用户身份信息
            if(Objects.isNull(token)){
                log.info("无效的token: {}", token);
                return invalidTokenMono(exchange);
            }
            boolean anyMatchBcStrings = false;
            if (!ObjectTools.isEmpty(whitelistConfig.getLists())){
                String path = exchange.getRequest().getPath().toString();
                 anyMatchBcStrings = whitelistConfig.getLists().stream().anyMatch(s -> path.contains(s));
            }
            if (!anyMatchBcStrings){
                String redisKey = String.format(USER_TOKEN,token,"-000001");
                Object userInfo = redisUtils.get(redisKey);
                if (ObjectTools.isNull(userInfo)){
                    log.info("无效的token: {}", token);
                    return invalidTokenMono(exchange);
                }
            }

            String clientId = exchange.getRequest().getHeaders().getFirst("client_id");
            String clientSecret = exchange.getRequest().getHeaders().getFirst("client_secret");
            // 取出用户身份信息
            if(Objects.isNull(token)){
                log.info("无效的token: {}", token);
                return invalidTokenMono(exchange);
            }
            // 给header里面添加值
            ServerHttpRequest tokenRequest = exchange.getRequest().mutate()
                    .header("access_token", token)
                    .header("client_id", clientId)
                    .header("client_secret", clientSecret)
                    .header("AUTHORIZATION","Bearer ".concat(token))
                    .build();
            ServerWebExchange build = exchange.mutate().request(tokenRequest).build();
            return getVoidMono(build, chain,starTime);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("无效的token: {}", token);
            return invalidTokenMono(exchange);
        }
    }


    /*****
     * 令牌解析
     */
    /*public static Map<String,Object> jwtVerify(String token,String clientIp){
        try {
            //解析Token
            Map<String, Object> dataMap = JwtToken.parseToken(token);
            //获取Token中IP的MD5
            String ip = dataMap.get("ip").toString();
            //比较Token中IP的MD5值和用户的IPMD5值
            if(ip.equals(MD5.md5(clientIp))){
                return dataMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * 无效的token
     */
    private Mono<Void> invalidTokenMono(ServerWebExchange exchange) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", HttpStatus.FORBIDDEN.value());
        hashMap.put("msg", "无效的token");
        return buildReturnMono(hashMap, exchange);
    }

    private Mono<Void> noTokenMono(ServerWebExchange exchange) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", HttpStatus.FORBIDDEN.value());
        hashMap.put("msg", "没有token");
        return buildReturnMono(hashMap, exchange);
    }

    private Mono<Void> buildReturnMono(HashMap<String, Object> hashMap, ServerWebExchange exchange) throws IOException {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = new ObjectMapper().writeValueAsString(hashMap).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // 指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
