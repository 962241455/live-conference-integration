package com.cmnt.dbpick.third.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import com.cmnt.dbpick.common.enums.AbleStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 第三方商户账号
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "third_sys_user")
@TypeAlias("third_sys_user")
public class ThirdSysUser extends BaseDocument {

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @ApiModelProperty("账号密码")
    private String password;

    @ApiModelProperty("商户套餐id")
    private String thirdMerchantPackageId;

    @ApiModelProperty("是否无限制")
    private Boolean limit;

    @ApiModelProperty("商户名称")
    private String thirdName;

    @ApiModelProperty("账号头像")
    private String thirdAvatar;

    @ApiModelProperty("盐值")
    private String salt;

    /**
     * @see AbleStatusEnum
     */
    @Indexed
    @ApiModelProperty("状态: disable-禁用,enable-启动")
    private String status;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
