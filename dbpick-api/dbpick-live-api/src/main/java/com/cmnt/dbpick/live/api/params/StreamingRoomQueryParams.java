package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessPageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "直播间查询参数")
public class StreamingRoomQueryParams extends ThirdAccessPageInfo {

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("房间状态：no_live-未开播 live_ing-直播中 forbidden-房间封禁 live_pause-直播暂停 live_over-直播结束")
    private String status;

    @ApiModelProperty("房间标题")
    private String roomTitle;

    @ApiModelProperty("查询开播开始时间")
    private String searchStartTime;
    @ApiModelProperty("查询开播结束时间")
    private String searchEndTime;

    @ApiModelProperty("查询创建开始时间戳")
    private Long createStartTime;
    @ApiModelProperty("查询创建结束时间戳")
    private Long createEndTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
