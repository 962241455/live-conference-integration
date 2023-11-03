package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "房间播放记录参数")
public class RoomPlayRecordParams extends ThirdAccessKeyInfo {

    @ApiModelProperty("房间号")
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.live.RoomTypeEnum
     */
    @ApiModelProperty("直播间类型： meeting-多人会议； live-单人直播； record-录播; third_push-三方推流")
    private String type;

    @ApiModelProperty("直播开始时间 yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty("直播停止时间 yyyy-MM-dd HH:mm:ss")
    private String stopTime;


}
