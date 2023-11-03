package com.cmnt.dbpick.third.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商户登录对象
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商户账号")
public class ThirdUserEditVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    private String id;

    @ApiModelProperty(value = "商户账号/用户账号")
    private String userId;

    @ApiModelProperty(value = "账号名称")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty("账号头像")
    private String userAvatar;

    @ApiModelProperty("状态: disable-禁用,enable-启动")
    private String status;

    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

}
