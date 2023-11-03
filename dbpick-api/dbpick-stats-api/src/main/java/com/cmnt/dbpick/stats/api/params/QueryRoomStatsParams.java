package com.cmnt.dbpick.stats.api.params;

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
@ApiModel(value = "直播间查询统计数据参数")
public class QueryRoomStatsParams extends ThirdAccessPageInfo {

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("第三方用户id")
    private String thirdUserId;

    @ApiModelProperty("观看类型: live-直播； playback-回放")
    private String watchType;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
