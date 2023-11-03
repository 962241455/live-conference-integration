package com.cmnt.dbpick.user.api.params;

import com.cmnt.dbpick.user.api.enums.FirmChannelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户参数类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
public class UserInfoParam  implements Serializable {

    private static final long serialVersionUID = -5948419802778187861L;


    @NotNull(message = "AccessKeyID 不能为空")
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @NotNull(message = "AccessKeySecret 不能为空")
    @ApiModelProperty("AccessKey Secret")
    private String sk;

    @NotNull(message = "用户ID 不能为空(第三方传入id， 不可重复)")
    @ApiModelProperty("用户 ID")
    private String userId;

    @NotNull(message = "用户昵称 不能为空")
    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @NotNull(message = "房间号 不能为空")
    @ApiModelProperty("房间号")
    private String roomNo;


    /**
     * @see com.cmnt.dbpick.common.enums.UserRoleEnum
     */
    @NotNull(message = "用户角色 不能为空")
    @ApiModelProperty("用户角色 startv:主播 | watch:观众 | major|管理")
    private String userRole;

    /**
     * @see com.cmnt.dbpick.user.api.enums.FirmChannelEnum
     */
    @ApiModelProperty("直播渠道")
    private String channe;



    public String getChanne() {
        if(StringUtils.isBlank(this.channe)){
            return FirmChannelEnum.TENLIVE_SETTING.getName();
        }
        return channe;
    }

}
