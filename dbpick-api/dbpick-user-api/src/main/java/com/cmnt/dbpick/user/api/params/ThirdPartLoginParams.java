package com.cmnt.dbpick.user.api.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther:
 * @Description: 第三方注册登录请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartLoginParams extends RegistBaseParam implements Serializable {

    private static final long serialVersionUID = -7630991410454441154L;

    /**
     * 微信access_token
     */
    private String accessToken;

    /**
     * 平台：1-IOS，2-Android 3- web
     */
    private String platform;

    /**
     * 来源：wx qq
     */
    private String source;

    /**
     * 微信open_id
     */
    private String identifier;


    /**
     * 腾讯unionid
     */
    private String unionid;

    /**
     * userId
     * 公众号需求授权登录无法获取token,通过参数传输UserId
     */
    private Integer userId;
}
