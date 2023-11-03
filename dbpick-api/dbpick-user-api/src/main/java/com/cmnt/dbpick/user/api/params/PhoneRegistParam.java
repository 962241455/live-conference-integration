package com.cmnt.dbpick.user.api.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-20 14:48
 * @Description: 手机注册参数类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneRegistParam extends RegistBaseParam implements Serializable {

    private static final long serialVersionUID = -5948519802778187861L;

    /**
     * 手机
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    private String shareId;
}
