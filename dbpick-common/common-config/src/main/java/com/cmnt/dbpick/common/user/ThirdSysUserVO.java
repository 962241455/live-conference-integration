package com.cmnt.dbpick.common.user;

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
public class ThirdSysUserVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    private String id;

    @ApiModelProperty(value = "商户账号")
    private String thirdId;

    @ApiModelProperty("商户名称")
    private String thirdName;

    @ApiModelProperty("账号头像")
    private String thirdAvatar;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty("状态: disable-禁用,enable-启动")
    private String status;

    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

    @ApiModelProperty("删除：1-删除、0-未删除")
    private Boolean deleted;
}
