package com.cmnt.dbpick.live.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shusong.liang
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "心跳检测参数")
public class HeartbeatParams {

    @NotBlank(message = "用户uid不能为空")
    @ApiModelProperty(value = "用户uid", required = true, example = "1")
    private String uid;

    @NotNull(message = "房间ID不能为空")
    @ApiModelProperty(value = "房间ID", required = true, example = "1")
    private Integer roomId;

    @Max(1)
    @Min(0)
    @NotNull(message = "角色不能为空")
    @ApiModelProperty(value = "角色（0-主播，1-主持人）", required = true, example = "1")
    private Integer role;
}
