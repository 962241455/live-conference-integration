package com.cmnt.dbpick.common.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("直播间默认配置对象")
public class RoomConfigVO implements Serializable {

    private static final long serialVersionUID = 101673413147308908L;

    private String id;

    @NotBlank(message = "房间标题不能为空")
    @ApiModelProperty("房间标题")
    private String title;

    @NotBlank(message = "房间logo封面不能为空")
    @ApiModelProperty("房间logo封面")
    private String logoCover;

    @NotBlank(message = "房间背景图不能为空")
    @ApiModelProperty("房间背景图")
    private String bgImg;

    @NotBlank(message = "默认主播头像不能为空")
    @ApiModelProperty("默认主播头像")
    private String streamerAvatar;

}
