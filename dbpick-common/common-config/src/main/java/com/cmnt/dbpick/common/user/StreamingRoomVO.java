package com.cmnt.dbpick.common.user;

import com.cmnt.dbpick.common.enums.SwitchEnum;
import com.cmnt.dbpick.common.enums.live.RoomLineTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * @author shusong.liang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("直播房间")
public class StreamingRoomVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;
    private String id;

    @ApiModelProperty("AccessKey ID")
    private String ak;

    @ApiModelProperty("商户账号")
    private String thirdId;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("直播分类")
    private String classify;

    @ApiModelProperty("开播模板")
    private String startvTPL;

    @ApiModelProperty("开播模板")
    private String watchTPL;

    @ApiModelProperty("聊天室id")
    private String chatRoomNo;

    @ApiModelProperty("房间标题")
    private String title;
    @ApiModelProperty("主办方")
    private String sponsor;

    @ApiModelProperty("房间logo封面")
//    private String logoCover;
    private Set<String> logoCover;

    @ApiModelProperty("房间背景图")
    private String bgImg;

    @ApiModelProperty("房间公告")
    private String announcement;

    @ApiModelProperty("直播场景")
    private String scene;

    /**
     * @see com.cmnt.dbpick.common.enums.live.RoomStatusEnum
     */
    @ApiModelProperty("房间状态：no_live-未开播 live_ing-直播中 forbidden-房间封禁 live_pause-直播暂停 live_over-直播结束")
    private String status;

    @ApiModelProperty("直播间类型： meeting-多人会议； live-单人直播； record-录播; third_push-三方推流")
    private String type;

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

    /**
     * @see com.cmnt.dbpick.common.enums.live.LiveWatchFilterTypeEnum
     */
    @ApiModelProperty("观看限制：no_filter-没有限制； register_filter-登记观看")
    private String watchFilter;

    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("游客观看开关限制：switch_close-关闭； switch_open-开启")
    private String visitorSwitch;

    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("回放开关：switch_close-关闭； switch_open-开启")
    private String playbackSwitch;
    @ApiModelProperty("回放过期时间(-1:永不过期)")
    private Long playbackTimeOut;

    @ApiModelProperty("水印Id")
    private Long watermarkId;

    @ApiModelProperty("开播时间")
    private String startTime;

    @ApiModelProperty("开播链接")
    private String playUrl;

    @ApiModelProperty("推流地址")
    private String pushUrl;
    @ApiModelProperty("推流地址过期时间")
    private String pushUrlTimeOut;

    @ApiModelProperty("游客观看页面")
    private String watchUrl;

    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

    @ApiModelProperty("创建时间戳")
    private String createUser;

    @ApiModelProperty("热度值")
    private Integer hotScores;

    @ApiModelProperty("在线人数")
    private Integer online;

    public String getLineType() {
        if(StringUtils.isBlank(this.lineType)){
            return RoomLineTypeEnum.CDN.getValue();
        }
        return lineType;
    }

    public Integer getHotScores() {
        if(Objects.isNull(this.hotScores) || this.hotScores<0){
            return 0;
        }
        return hotScores;
    }

    public Integer getOnline() {
        if(Objects.isNull(this.online) || this.online<0){
            return 0;
        }
        return online;
    }

    public String getPlaybackSwitch() {
        if(StringUtils.isBlank(this.playbackSwitch)){
            return SwitchEnum.CLOSE.getValue();
        }
        return playbackSwitch;
    }
}
