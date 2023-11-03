package com.cmnt.dbpick.user.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther:
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniprogramLoginVo implements Serializable {

    private static final long serialVersionUID = 6021349426272125521L;
    private String token;
    private String rcloudToken;
    private String openid;
    private String unionid;
    private String refreshToken;
}
