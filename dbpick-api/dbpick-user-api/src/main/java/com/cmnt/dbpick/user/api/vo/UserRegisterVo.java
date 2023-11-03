package com.cmnt.dbpick.user.api.vo;

import com.cmnt.dbpick.common.enums.live.RoomLineTypeEnum;
import com.cmnt.dbpick.common.user.UserTokenInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户信息凭证
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterVo implements Serializable {

    @ApiModelProperty("用户 ID")
    private String id;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("用户头像")
    private String userAvatar;
    /**
     * @see com.cmnt.dbpick.common.enums.UserSourceEnum
     */
    @ApiModelProperty("用户来源")
    private String userSource;

    @ApiModelProperty("邮箱")
    private String email;
    /**
     * @see
     */
    @ApiModelProperty("性别： m-男， f-女， unknown-未知")
    private String gender;

    /**
     * @see com.cmnt.dbpick.common.enums.PlatformEnum
     */
    @ApiModelProperty(value = "用户来源平台：1- web 2- android 3-ios 4-小程序")
    private String platform;
    /**
     * @see com.cmnt.dbpick.common.enums.UserRoleEnum
     */
    @ApiModelProperty("用户角色 startv:主播 | watch:观众 | major|管理")
    private String userRole;

    @ApiModelProperty(value = "tokenInfo", hidden = true)
    private UserTokenInfoVo tokenInfoVo;

    @ApiModelProperty("userSig")
    private String userSig;
    @ApiModelProperty("shareUserSig")
    private String shareUserSig;
    @ApiModelProperty("jwtToken")
    private String jwtToken;

    @ApiModelProperty("商户id")
    private String thirdId;

    @ApiModelProperty("是否是会议操作员：0 -否 1-是")
    private int checktOperate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
