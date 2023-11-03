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
 * 商户子账号
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "third_user")
@TypeAlias("third_user")
public class ThirdUser extends BaseDocument {

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @Indexed
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
    @Indexed
    @ApiModelProperty("状态: disable-禁用,enable-启动")
    private String status;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
