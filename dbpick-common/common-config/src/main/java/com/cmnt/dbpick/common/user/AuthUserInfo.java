package com.cmnt.dbpick.common.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author
 * @Description: python 用户信息
 * @dateTime
 */
@Slf4j
@Data
public class AuthUserInfo implements Serializable {

    private static final long serialVersionUID = 1495413060843290738L;

    private int id;
    private String account;
    /**
     * 用户手机号
     */
    private String phone;
    private String nickname;
    private String avatar;
    private int sex;
    /**
     * 用户状态
     */
    private int status;
    private String chickenId;
    /**
     * access_token
     */
    private String accessToken;
    /**
     * 用户创建时间
     */
    private long createTime;

    public AuthUserInfo() {
    }
    /**
     * 默认的头像链接, 临时需求
     */
    public static final String DEFAULT_AVATAR = "";

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

