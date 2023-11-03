package com.cmnt.dbpick.user.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPhoneLoginVo {

    /**
     * 用户token
     */
    private String token;

    private String rcloudToken;

    /**
     * true 验证码有效 \ false 验证码无效	是
     */
    private boolean isValid;

    private String refreshToken;

}
