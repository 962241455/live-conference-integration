package com.cmnt.dbpick.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cmnt.dbpick.common.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JwtToken {

    //默认秘钥
    private static final String DEFAULT_SECRET="d4dM54c75d4afabY80dc47c8f4dde862";

    /***
     * 创建Jwt令牌
     * 秘钥：secret
     * 载荷:dataMap(Map)
     */
    public static String createToken(Map<String,Object> dataMap){
        return createToken(dataMap,null);
    }
    /***
     * 创建Jwt令牌
     * 秘钥：secret
     * 载荷:dataMap(Map)
     */
    public static String createToken(Map<String,Object> dataMap, String secret){
        //确认秘钥
        if(StringUtils.isEmpty(secret)){
            secret = DEFAULT_SECRET;
        }

        //确认签名算法
        Algorithm algorithm = Algorithm.HMAC256(secret);

        //jwt令牌创建
        return
        JWT.create()
                .withClaim("body",dataMap)  //自定义载荷
                .withIssuer("GP")   //签发者
                .withSubject("JWT令牌")   //主题
                .withAudience("member") //接收方
                .withExpiresAt(new Date(DateUtil.getTimeStrampSeconds()+30*24*60*60*1000L))    //过期时间 30天
                .withNotBefore(new Date(DateUtil.getTimeStrampSeconds()+0))       //1秒后才能使用
                .withIssuedAt(new Date())   //签发时间
                .withJWTId(UUID.randomUUID().toString().replace("-",""))    //唯一标识符
                .sign(algorithm);
    }

    /****
     * 令牌解析
     */
    public static Map<String,Object> parseToken(String token){
        return parseToken(token,null);
    }
    /****
     * 令牌解析
     */
    public static Map<String,Object> parseToken(String token,String secret){
        //确认秘钥
        if(StringUtils.isEmpty(secret)){
            secret = DEFAULT_SECRET;
        }

        //确认签名算法
        Algorithm algorithm = Algorithm.HMAC256(secret);

        //创建令牌校验对象
        JWTVerifier verifier = JWT.require(algorithm).build();
        //校验解析
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("body").as(Map.class);
    }

    public static void main(String[] args) throws InterruptedException {

       /* //创建令牌
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("name","zhangsan");
        dataMap.put("address","湖南");
        dataMap.put("jsj","111");
        dataMap.put("kjyd","");

        //创建令牌
        String token = createToken(dataMap);
        System.out.println(token);

        //休眠一秒钟
        TimeUnit.SECONDS.sleep(1);*/

        /*String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKV1Tku6TniYwiLCJhdWQiOiJtZW1iZXIiLCJuYmYiOjE2NTk0OTQ5MjcsImlzcyI6IkdQIiwiYm9keSI6eyJ1c2VyQXZhdGFyIjoiaGhoIiwidXNlck5hbWUiOiLlha0iLCJ1c2VyUm9sZSI6IndhdGNoIiwidXNlcklkIjoiNjJlOWQ1OTRmMDhjZjM3MTM4NGMyMDlmIiwicm9vbUlkIjoiMTAwMDcifSwiZXhwIjoxNjU5OTI2OTI3LCJpYXQiOjE2NTk0OTQ5MjcsImp0aSI6IjA2YmE5M2QwMmZiMzRkYTg5OWFkYTcyYzc4NTY1OTk1In0.mSljsdpg9bt8-ywK1Wo_FJBMjaaEyuzDJpMwYMm1jaM";
        //校验解析令牌
        Map<String, Object> stringObjectMap = parseToken(token);
        System.err.println(stringObjectMap);*/

        //{"roomNo":"830001","title":"\u623F\u95F4\u6807\u9898","userAvatar":"http\u5934\u50CF","userId":"62e9edbc5c9a7a058b8958e8","userName":"\u516D","userRole":"watch"}
        System.err.println(parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKV1Tku6TniYwiLCJhdWQiOiJtZW1iZXIiLCJuYmYiOjE2NjE0OTg4MTIsImlzcyI6IkdQIiwiYm9keSI6eyJyb29tTm8iOiIxMDAwNSIsInRoaXJkSWQiOiI2MzA4NzViYzJiZmIxMTU1NjBmYjIxMDgiLCJ1c2VyQXZhdGFyIjoiaHR0cDovL3d3dy5jbW50LmNuL2Fzc2V0cy9pbWcvbG9nby5wbmciLCJ0aXRsZSI6IjA4Mjjnm7Tmkq0iLCJ1c2VyTmFtZSI6IuWKszEiLCJ1c2VyUm9sZSI6InN0YXJ0diIsInVzZXJJZCI6IjYzMDg3NWJjMmJmYjExNTU2MGZiMjEwOCJ9LCJleHAiOjE2NjQwOTA4MTIsImlhdCI6MTY2MTQ5ODgxMiwianRpIjoiNzg0NDlmZWZlYzM1NGYxNTk5NGI3MzhiODZlM2M0MDIifQ.1sp73Ke0VrWAGBUyrmNzbG-wIGeYZkZPb9aGkIg0Yis"));
        System.err.println(parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKV1Tku6TniYwiLCJhdWQiOiJtZW1iZXIiLCJuYmYiOjE2NjE0OTU5NDAsImlzcyI6IkdQIiwiYm9keSI6eyJyb29tTm8iOiIxMDAwNSIsInRoaXJkSWQiOiI2MzA4NmE4NDJiZmIxMTU1NjBmYjIxMDIiLCJ1c2VyQXZhdGFyIjoiaHR0cDovL3d3dy5jbW50LmNuL2Fzc2V0cy9pbWcvbG9nby5wbmciLCJ0aXRsZSI6IjA4Mjjnm7Tmkq0iLCJ1c2VyTmFtZSI6IjEwMDA15Li75oyB5Lq6IiwidXNlclJvbGUiOiJtYWpvciIsInVzZXJJZCI6IjYzMDg2YTg0MmJmYjExNTU2MGZiMjEwMiJ9LCJleHAiOjE2NjQwODc5NDAsImlhdCI6MTY2MTQ5NTk0MCwianRpIjoiODg2MTFiMDg0ZjRjNGZiMGFkMThkYjk3ZjE4MjYyMWIifQ.9Y4A7Ym9I2lhNQWRFLFMJWDyIDjEAR8FOhDEYw43ZhE"));



    }
}
