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
 * 平台访问凭证
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "third_access_key")
@TypeAlias("third_access_key")
public class ThirdAccessKey extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String accessKeyId;

    @ApiModelProperty("AccessKey Secret")
    private String accessKeySecret;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @ApiModelProperty("应用名称")
    private String platform;

    @ApiModelProperty("应用名称")
    private String appName;

    @ApiModelProperty("过期时间戳，-1=永远有效")
    private Long expireTime;

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
