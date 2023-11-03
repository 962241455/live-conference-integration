package com.cmnt.dbpick.user.api.params;

import com.cmnt.dbpick.common.constant.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
public class UserRegisterParam {

    @NotNull(message = "登录类型不能为空")
    @ApiModelProperty("登录类型：pwd-密码/sms-短信")
    private String loginType;

    @ApiModelProperty(value = "短信类型：check-校验/login-登录/register-注册/cancel-注销",hidden = true)
    private String smsType = "login";

    @NotNull
    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("账号密码")
    private String password;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty(value = "用户来源平台：1- web 2- android 3-ios 4-小程序")
    private String platform;

    @ApiModelProperty("商户id")
    private String thirdId;

    @ApiModelProperty(value = "注册ip", hidden = true)
    private String registerIP;

    @ApiModelProperty(value = "是否需要校验验证码(管理员添加用户，需要跳过短信验证)", hidden = true)
    private Boolean smsCheckStatus;

    @ApiModelProperty("验证码")
    private String smsCode;

}