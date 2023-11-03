package com.cmnt.dbpick.common.model;

import com.cmnt.dbpick.common.page.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商户认证信息")
public class ThirdAccessPageInfo extends PageRequest {

//    @ApiModelProperty(value = "AccessKey ID", hidden = true)
//    private String ak;

    @ApiModelProperty(value = "商户账号", hidden = true)
    private String thirdId;

    @ApiModelProperty(value = "操作员账号", hidden = true)
    private String createUser;

}
