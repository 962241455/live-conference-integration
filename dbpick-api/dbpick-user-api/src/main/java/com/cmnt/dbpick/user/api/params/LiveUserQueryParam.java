package com.cmnt.dbpick.user.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessPageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 主持人/主播查询参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "主持人/主播查询参数")
public class LiveUserQueryParam extends ThirdAccessPageInfo {

    @ApiModelProperty(value = "直播房间号")
    private String roomNo;


    @NotBlank(message = "商户账号不能为空")
    @ApiModelProperty("商户账号")
    private String thirdId;

    @NotBlank(message = "AccessKey ID不能为空")
    @ApiModelProperty("AccessKey ID")
    private String ak;



}
