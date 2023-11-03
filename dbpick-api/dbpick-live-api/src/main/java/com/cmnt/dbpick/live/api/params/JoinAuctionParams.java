package com.cmnt.dbpick.live.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author shusong.liang
 */
@Data
@ApiModel(value = "主持人/主播上麦参数")
public class JoinAuctionParams {

    @NotNull(message = "直播房间ID不能为空")
    @ApiModelProperty(value = "直播房间ID", required = true, example = "1")
    private Integer roomId;

    @Min(1)
    @Max(2)
    @NotNull(message = "上麦身份不能为空")
    @ApiModelProperty(value = "上麦身份（1-主持人，2-主播）", required = true, example = "1")
    private Integer identity;
}
