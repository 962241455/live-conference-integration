package com.cmnt.dbpick.third.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "商户套餐参数")
public class MerchantPackageEditParam implements Serializable {

    @ApiModelProperty("套餐id 修改时必传")
    private String id;

    @NotBlank(message = "套餐名称不能为空")
    @ApiModelProperty("套餐名称")
    private String name;

    @NotNull(message = "可同时开播数不能为空")
    @ApiModelProperty("可同时开播数")
    private Integer broadcastingRoom;

    @NotNull(message = "可增加临时房间数量不能为空")
    @ApiModelProperty("可增加临时房间数量")
    private Integer temporaryRoom;

    @NotNull(message = "存储空间大小不能为空")
    @ApiModelProperty("存储空间")
    private Integer storageSpace;

    @NotNull(message = "流收费标准不能为空")
    @ApiModelProperty("流收费标准(分/BG)")
    private Long rates;

    @NotBlank(message = "转码时长不能为空")
    @ApiModelProperty("转码时长 无限：free ")
    private String transcodingDuration;

    @NotBlank(message = "支持功能不能为空")
    @ApiModelProperty("支持功能")
    private String supportingFunction;

    @NotNull(message = "支持观看人数不能为空")
    @ApiModelProperty("支持观看人数")
    private Integer viewableAudience;

    @NotNull(message = "短信数量不能为空")
    @ApiModelProperty("短信数量")
    private Integer getSimNum;

    @NotNull(message = "套餐价格不能为空")
    @ApiModelProperty("套餐价格 分")
    private Long packagePrice;

}
