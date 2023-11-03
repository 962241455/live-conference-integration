package com.cmnt.dbpick.live.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shusong.liang
 */
@Data
@ApiModel(value = "主播处理上麦申请参数")
public class ProcessApplicationParams {

    @NotBlank(message = "拍卖号不能为空")
    @ApiModelProperty(value = "拍卖号", required = true, example = "1")
    private String auctionNo;

    @NotNull(message = "处理结果不能为空")
    @ApiModelProperty(value = "处理结果（true-接受，false-退场）", required = true, example = "true", allowableValues = "true,false")
    private Boolean access;
}
