package com.cmnt.dbpick.common.user;

import com.cmnt.dbpick.common.entity.BaseDocument;
import com.cmnt.dbpick.common.enums.AbleStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 商户子账号
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商户子账号")
public class ThirdUserVO extends BaseDocument {

    private String id;

    @ApiModelProperty("商户账号")
    private String thirdId;

    @ApiModelProperty("用户账号")
    private String userId;

    @ApiModelProperty("密码")
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

    @ApiModelProperty("删除：1-删除、0-未删除")
    private Boolean deleted;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
