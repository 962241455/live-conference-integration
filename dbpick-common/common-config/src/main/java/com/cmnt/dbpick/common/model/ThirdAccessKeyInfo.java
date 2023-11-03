package com.cmnt.dbpick.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商户认证信息")
public class ThirdAccessKeyInfo {

    @ApiModelProperty(value = "AccessKey ID",hidden = true)
    private String ak;
//
//    @ApiModelProperty(value = "AccessKey Secret",hidden = true)
//    private String sk;

    @ApiModelProperty(value = "商户账号",hidden = true)
    private String thirdId;

    @ApiModelProperty(value = "操作员账号",hidden = true)
    private String createUser;

}
