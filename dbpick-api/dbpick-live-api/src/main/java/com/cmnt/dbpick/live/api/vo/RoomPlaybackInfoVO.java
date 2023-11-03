package com.cmnt.dbpick.live.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("直播间回放信息")
public class RoomPlaybackInfoVO implements Serializable {


    @ApiModelProperty("房间状态：no_live-no_live-未开播 " +
            "live_ing-直播中 forbidden-房间封禁 live_pause-直播暂停 live_over-直播结束")
    private String roomStatus;

    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("回放开关：switch_close-关闭； switch_open-开启")
    private String playbackSwitch;
    @ApiModelProperty("回放过期时间(-1:永不过期)")
    private Long playbackTimeOut;


    @ApiModelProperty("视频列表")
    private List<RoomPlaybackVO> playbackVideos;

}
