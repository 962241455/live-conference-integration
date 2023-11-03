package com.cmnt.dbpick.user.api.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-20 14:48
 * @Description: web注册参数类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class WbeUserInfoParam implements Serializable {

    private static final long serialVersionUID = -5948419802778187861L;

    @NotNull(message = "AccessKeyID 不能为空")
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @NotNull(message = "AccessKeySecret 不能为空")
    @ApiModelProperty("AccessKey Secret")
    private String sk;

    @NotNull(message = "房间号 不能为空")
    @ApiModelProperty("房间号")
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.UserRoleEnum
     */
    @NotNull(message = "用户角色 不能为空")
    @ApiModelProperty("用户角色 startv:主播 | watch:观众 | major|管理")
    private String userRole;
}
