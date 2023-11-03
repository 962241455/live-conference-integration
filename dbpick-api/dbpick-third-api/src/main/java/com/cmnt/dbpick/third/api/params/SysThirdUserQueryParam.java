package com.cmnt.dbpick.third.api.params;

import com.cmnt.dbpick.common.page.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "商户账号查询参数")
public class SysThirdUserQueryParam extends PageRequest {

    @ApiModelProperty("商户账号")
    private String thirdId;

    @ApiModelProperty("商户名称")
    private String thirdName;

    @ApiModelProperty("状态: disable-禁用,enable-启动")
    private String status;

}
