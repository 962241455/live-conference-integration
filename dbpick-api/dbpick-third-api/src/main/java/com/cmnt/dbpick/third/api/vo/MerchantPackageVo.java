package com.cmnt.dbpick.third.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商户套餐
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPackageVo implements Serializable {
    @ApiModelProperty("套餐名称")
    private String name;
    @ApiModelProperty("可同时开播数")
    private Integer broadcastingRoom;
    @ApiModelProperty("可增加临时房间数量")
    private Integer temporaryRoom;
    @ApiModelProperty("存储空间")
    private Integer storageSpace;
    @ApiModelProperty("流收费标准(分/BG)")
    private Long rates;
    @ApiModelProperty("转码时长 无限：free ")
    private String transcodingDuration;
    @ApiModelProperty("支持功能")
    private String supportingFunction;
    @ApiModelProperty("支持观看人数")
    private Integer viewableAudience;
    @ApiModelProperty("短信数量")
    private Integer getSimNum;
    @ApiModelProperty("套餐价格 分")
    private Long packagePrice;
}
