package com.cmnt.dbpick.user.api.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author
 * @Description: 手机登录参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneLoginParams implements Serializable {

    private static final long serialVersionUID = -8751662703075392598L;

    /**
     *  用户信息
     */
    private String userAgent;

    /**
     *  渠道号
     */
    private String channel;

    /**
     *  验证码
     */
    private String code;

    /**
     *  设备号
     */
    private String deviceId;

    /**
     *  手机号
     */
    private String phone;

    /**
     *  hmacSHA1加密验签
     */
    private String sign;

    /**
     * h5请求标识
     * */
    private String h5Naughty;

}
