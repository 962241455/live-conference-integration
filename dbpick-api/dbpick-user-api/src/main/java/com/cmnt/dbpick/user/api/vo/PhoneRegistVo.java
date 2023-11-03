package com.cmnt.dbpick.user.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther:
 * @Description: 手机注册相应类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneRegistVo implements Serializable {

    private static final long serialVersionUID = 2938499262980135763L;

    private String token;

    private String rcloudToken;

    private String said;

    private String refreshToken;
}
