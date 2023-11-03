package com.cmnt.dbpick.common.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 用户基本信息
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseInfo implements Serializable {

    private static final long serialVersionUID = 1495413060843290738L;

    @ApiModelProperty("用户 ID")
    private String userId;

    @ApiModelProperty("第三方用户 ID")
    private String thirdUserId;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    /**
     * @see com.cmnt.dbpick.common.enums.UserRoleEnum
     */
    @ApiModelProperty("用户角色 startv:主播 | watch:观众 | major|管理")
    private String userRole;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

