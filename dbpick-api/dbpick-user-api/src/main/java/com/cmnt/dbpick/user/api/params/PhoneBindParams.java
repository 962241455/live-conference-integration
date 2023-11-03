package com.cmnt.dbpick.user.api.params;

import lombok.Data;

import java.io.Serializable;

/**
 * 手机绑定参数
 *
 * @author yangshidong
 * @date 2018/10/24
 */
@Data
public class PhoneBindParams implements Serializable {

    private static final long serialVersionUID = 5902683012521464619L;

    /**
     * 性别:1 男，2 女
     */
    private int sex;

    /**
     * 验证码
     */
    private String code;

    /**
     * 手机号
     */
    private String phone;
}
