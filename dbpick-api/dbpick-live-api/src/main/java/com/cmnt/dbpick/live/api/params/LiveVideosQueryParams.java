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
@ApiModel(value = "直播视频库查询参数")
public class LiveVideosQueryParams extends ThirdAccessPageInfo {

    @ApiModelProperty("文件名")
    private String fileName;
    @ApiModelProperty("视频id")
    private String fileId;

    /**
     * @see com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum
     */
    @ApiModelProperty("NORMAL-未转码； PROCESSING-转码中； FINISH- 转码完成；FAILED- 转码失败")
    private String transcodeStatus;

    @ApiModelProperty(value = "结束时间", hidden = true)
    private String endTime;
    @ApiModelProperty(value = "房间号", hidden = true)
    private String roomNo;


    @ApiModelProperty("查询开始时间戳")
    private Long searchStartTime;
    @ApiModelProperty("查询结束时间戳")
    private Long searchEndTime;


    @ApiModelProperty("查询转码开始时间戳")
    private Long searchStartTimeTrans;
    @ApiModelProperty("查询转码结束时间戳")
    private Long searchEndTimeTrans;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
