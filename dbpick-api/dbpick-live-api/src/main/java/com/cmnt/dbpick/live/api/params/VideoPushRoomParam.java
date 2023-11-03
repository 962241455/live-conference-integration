package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "视频推送到直播间参数")
public class VideoPushRoomParam extends ThirdAccessKeyInfo {

    private String id;

    @NotBlank(message = "视频信息id为空")
    @ApiModelProperty("视频信息id")
    private String videoId;

    @NotBlank(message = "开播时间为空")
    @ApiModelProperty("开播时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @NotBlank(message = "房间号为空")
    @ApiModelProperty("房间号")
    private String roomNo;
    /**
     * @see com.cmnt.dbpick.common.enums.PullStreamStatusEnum
     */
    @ApiModelProperty("任务状态: enable - 启用, pause - 暂停。")
    private String status;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
