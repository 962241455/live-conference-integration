package com.cmnt.dbpick.stats.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "分析直播间观看数据参数")
public class AnalyseWatchTimesParam extends ThirdAccessKeyInfo {

    @ApiModelProperty("房间号")
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum
     */
    @ApiModelProperty("观看类型: live-直播； playback-回放； all-直播+回放")
    private String watchType;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
