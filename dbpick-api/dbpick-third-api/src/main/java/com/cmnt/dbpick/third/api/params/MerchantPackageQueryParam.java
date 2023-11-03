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
public class MerchantPackageQueryParam extends PageRequest {

    @ApiModelProperty("套餐名称")
    private String name;

}
