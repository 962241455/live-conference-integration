package com.cmnt.dbpick.third.api.params;

import com.cmnt.dbpick.common.constant.Constants;
import com.cmnt.dbpick.common.enums.AbleStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 商户子账号
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商户子账号")
public class ThirdUserEditParam implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    private String id;

    @NotBlank(message = "商户账号不能为空")
    @ApiModelProperty("商户账号")
    private String thirdId;

    @NotBlank(message = "用户账号不能为空")
    @ApiModelProperty("用户账号")
    private String userId;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("账号头像")
    private String userAvatar;

    /**
     * @see AbleStatusEnum
     */
    @ApiModelProperty("状态: disable-禁用,enable-启动")
    private String status;

    public String getUserName() {
        if(StringUtils.isBlank(this.userName)){
            return this.userId;
        }
        return this.userName;
    }

    public String getUserAvatar() {
        if(StringUtils.isBlank(this.userAvatar)){
            return Constants.getDefaultImgae();
        }
        return userAvatar;
    }

    @ApiModelProperty("创建人")
    private String createUser;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
