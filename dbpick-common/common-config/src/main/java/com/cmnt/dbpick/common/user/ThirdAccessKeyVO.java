package com.cmnt.dbpick.common.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 访问凭证
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdAccessKeyVO implements Serializable {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty(value = "商户账号")
    private String thirdId;

    @ApiModelProperty("AccessKey ID")
    private String accessKeyId;

    @ApiModelProperty("AccessKey Secret")
    private String accessKeySecret;

    @ApiModelProperty("状态: disable-禁用,enable-启动")
    private String status;

    @ApiModelProperty("平台")
    private String platform;

    @ApiModelProperty("应用名称")
    private String appName;

    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

    @ApiModelProperty("删除：1-删除、0-未删除")
    private Boolean deleted;

}
