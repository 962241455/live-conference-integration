package com.cmnt.dbpick.user.api.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
public class UserDefaultRegisterParam {

    @NotNull(message = "登录类型不能为空")
    @ApiModelProperty("登录类型：pwd-密码/sms-短信/def-默认")
    private String loginType;

    @NotNull
    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty(value = "用户来源平台：1- web 2- android 3-ios 4-小程序")
    private String platform;

    @ApiModelProperty("商户id")
    private String thirdId;

    @ApiModelProperty(value = "注册ip", hidden = true)
    private String registerIP;

    @ApiModelProperty("邮箱")
    private String email;

    /**
     * @see
     */
    @ApiModelProperty("性别： 1-男， 2-女， 0-未知")
    private Integer gender;

}