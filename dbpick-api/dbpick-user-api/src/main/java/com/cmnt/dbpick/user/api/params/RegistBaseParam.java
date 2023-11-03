package com.cmnt.dbpick.user.api.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther
 * @Description: 注册登录基础类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistBaseParam implements Serializable {

    private static final long serialVersionUID = -106189322824823915L;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * userAgent, 逗号分隔,iOS 10.3,devik,xxxxx
     */
    private String userAgent;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 地区
     */
    private String region;

    /**
     * 用户名
     */
    private String username;

    /**
     * 性别：0-未知 男-1 女-2
     */
    private Integer sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 平台：1-IOS，2-Android 3- web
     */
    private String platform;

    /**
     * 用户账号 uid
     */
    private String account;

    /**
     * 邀请人uid
     */
    private String invitingUid;
}
