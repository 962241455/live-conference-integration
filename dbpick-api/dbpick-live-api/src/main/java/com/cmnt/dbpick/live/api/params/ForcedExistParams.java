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
@ApiModel(value = "强制下麦参数")
public class ForcedExistParams {

    @NotNull(message = "直播房间ID不能为空")
    @ApiModelProperty(value = "直播房间ID", required = true, example = "1")
    private Integer roomId;

    @NotBlank(message = "被强制下麦的uid不能为空")
    @ApiModelProperty(value = "被强制下麦的uid", required = true, example = "1")
    private String uid;
}
