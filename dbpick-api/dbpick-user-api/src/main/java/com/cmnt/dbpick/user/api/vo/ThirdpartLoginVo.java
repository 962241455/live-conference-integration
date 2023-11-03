package com.cmnt.dbpick.user.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-12 15:16
 * @Description: 第三方注册登录返回参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdpartLoginVo implements Serializable {

    private static final long serialVersionUID = -8609527769011431227L;

    private String token;

    private String rcloudToken;

    private String said;

    private boolean phoneBind;

    private String refreshToken;
}
