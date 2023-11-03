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
@ApiModel(value = "主持人邀请主播上麦参数")
public class InviteParams {

    @NotBlank(message = "拍卖号不能为空")
    @ApiModelProperty(value = "拍卖号", required = true, example = "1")
    private String auctionNo;

    @NotNull(message = "是否邀请不能为空")
    @ApiModelProperty(value = "是否邀请（true-邀请，false-撤销邀请）", required = true, example = "true", allowableValues = "true,false")
    private Boolean isInvite;
}
