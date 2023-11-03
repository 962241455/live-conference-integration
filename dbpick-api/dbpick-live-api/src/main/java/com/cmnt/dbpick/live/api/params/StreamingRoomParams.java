package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "直播房间创建参数")
public class StreamingRoomParams extends ThirdAccessKeyInfo {

    private String id;

    @ApiModelProperty("房间标题")
    private String title;
    @ApiModelProperty("主办方")
    private String sponsor;

    @ApiModelProperty("直播分类")
    private String classify;

    @ApiModelProperty("直播场景")
    private String scene;

    @ApiModelProperty("开播模板")
    private String startvTPL;

    @ApiModelProperty("观看模板")
    private String watchTPL;

    /**
     * @see com.cmnt.dbpick.common.enums.live.RoomTypeEnum
     */
    @ApiModelProperty("直播间类型： meeting-多人会议； live-单人直播； record-录播; third_push-三方推流")
    private String type;

    @ApiModelProperty("房间logo封面")
    private String logoCover;

    @ApiModelProperty("房间背景图")
    private String bgImg;

    @ApiModelProperty("房间公告")
    private String announcement;

    /**
     * @see com.cmnt.dbpick.common.enums.live.RoomRecordedEnum
     */
    @ApiModelProperty("是否开启录制回放: recorded-开启录制; unrecorded-不录制")
    private String recordedStatus;

    /**
     * @see com.cmnt.dbpick.common.enums.live.RoomLineTypeEnum
     */
    @ApiModelProperty("延迟类型: leb 快直播， cdn 标准直播")
    private String lineType;


    @ApiModelProperty("开播时间")
    private String startTime;


    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("回放开关：switch_close-关闭； switch_open-开启")
    private String playbackSwitch;
    @ApiModelProperty("回放过期时间(-1:永不过期)")
    private Long playbackTimeOut;


    public Long getPlaybackTimeOut() {
        if(Objects.isNull(this.playbackTimeOut)){
            return -1L;
        }
        return playbackTimeOut;
    }
}
