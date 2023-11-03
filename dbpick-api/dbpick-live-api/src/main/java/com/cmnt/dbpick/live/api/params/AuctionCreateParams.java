package com.cmnt.dbpick.live.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shusong.liang
 */
@Data
@ApiModel(value = "申请上麦参数")
public class AuctionCreateParams {

    @NotNull(message = "直播房间ID不能为空")
    @ApiModelProperty(value = "直播房间ID", required = true, example = "1")
    private Integer roomId;

    @NotBlank(message = "服务内容不能为空")
    @ApiModelProperty(value = "服务内容", required = true, example = "王者荣耀陪玩一个月")
    private String description;

    @Max(value = 1_000_000, message = "起拍价不能大于10000")
    @Min(value = 0, message = "起拍价不能小于0")
    @NotNull(message = "起拍价不能为空")
    @ApiModelProperty(value = "起拍价（分）", required = true, example = "10000")
    private Integer price;

    @NotBlank(message = "自拍不能为空")
    @ApiModelProperty(value = "自拍", required = true)
    private String img;
}
