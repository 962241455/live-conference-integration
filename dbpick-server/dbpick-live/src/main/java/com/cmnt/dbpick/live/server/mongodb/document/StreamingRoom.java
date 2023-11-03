package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import com.cmnt.dbpick.live.api.model.StreamingRoomInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * 直播间
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "streaming_room")
@TypeAlias("streaming_room")
public class StreamingRoom extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @Indexed
    @ApiModelProperty("房间号")
    private String roomNo;
    /**
     * @see com.cmnt.dbpick.common.enums.live.RoomTypeEnum
     */
    @ApiModelProperty("直播间类型： meeting-多人会议； live-单人直播； record-录播; third_push-三方推流")
    private String type;

    @ApiModelProperty("直播分类")
    private String classify;
    /**
     * @see com.cmnt.dbpick.common.enums.live.RoomStatusEnum
     */
    @Indexed
    @ApiModelProperty("房间状态：no_live-未开播 live_ing-直播中 forbidden-房间封禁 live_pause-直播暂停 live_over-直播结束")
    private String status;
    /**
     * @see com.cmnt.dbpick.common.enums.live.LiveWatchFilterTypeEnum
     */
    @ApiModelProperty("观看限制：no_filter-没有限制； register_filter-登记观看；auth_filter-验证码观看内容")
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

    @ApiModelProperty("房间详情")
    private StreamingRoomInfo info;


    public Long getPlaybackTimeOut() {
        if(Objects.isNull(this.playbackTimeOut)){
            return -1L;
        }
        return playbackTimeOut;
    }
}
