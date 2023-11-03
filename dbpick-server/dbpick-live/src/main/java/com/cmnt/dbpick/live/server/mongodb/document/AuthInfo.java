package com.cmnt.dbpick.live.server.mongodb.document;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description 验证码观看内容
 * @date 2022-08-27 16:52
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthInfo {

    @ApiModelProperty("欢迎标题")
    private  String title;

    @ApiModelProperty("验证码")
    private String code;
}
