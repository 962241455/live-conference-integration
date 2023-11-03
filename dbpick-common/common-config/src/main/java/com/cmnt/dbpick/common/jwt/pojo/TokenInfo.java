package com.cmnt.dbpick.common.jwt.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 用户信息凭证
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo implements Serializable {

    @ApiModelProperty("商户账号")
    private String thirdId;

    @ApiModelProperty("用户 ID")
    private String userId;

//    @ApiModelProperty("用户昵称")
//    private String userName;
//
//    @ApiModelProperty("用户头像")
//    private String userAvatar;

    @ApiModelProperty("房间号")
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.UserRoleEnum
     */
    @ApiModelProperty("用户角色 startv:主播 | watch:观众 | major|管理")
    private String userRole;

    public String getThirdId() {
        if(StringUtils.isBlank(this.thirdId) && StringUtils.isNotBlank(this.userId)){
            return userId;
        }
        return thirdId;
    }

    public String getUserId() {
        if(StringUtils.isBlank(this.userId) && StringUtils.isNotBlank(this.thirdId)){
            return thirdId;
        }
        return userId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
