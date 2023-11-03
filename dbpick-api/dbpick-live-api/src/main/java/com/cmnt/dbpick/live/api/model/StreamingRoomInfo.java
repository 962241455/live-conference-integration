package com.cmnt.dbpick.live.api.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * 直播间其他参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamingRoomInfo implements Serializable {


    @ApiModelProperty("直播场景")
    private String scene;

    @ApiModelProperty("开播模板")
    private String startvTPL;

    @ApiModelProperty("观看模板")
    private String watchTPL;

    @ApiModelProperty("聊天室No")
    private String chatRoomNo;

    @ApiModelProperty("房间标题")
    private String title;
    @ApiModelProperty("主办方")
    private String sponsor;

    @ApiModelProperty("房间logo封面列表")
    private Set<String> logoCover;

    @ApiModelProperty("房间背景图")
    private String bgImg;

    @ApiModelProperty("房间介绍")
    private String announcement;

    @ApiModelProperty("预计开播时间(纯展示)")
    private String startTime;

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

    @ApiModelProperty("水印Id(-1 表示沒有水印)")
    private Long watermarkId;

    @ApiModelProperty("开播链接")
    private String playUrl;
    @ApiModelProperty("推流地址")
    private String pushUrl;
    @ApiModelProperty("推流地址过期时间")
    private String pushUrlTimeOut;


    @ApiModelProperty("热度值")
    private Integer hotScores;

    @ApiModelProperty("在线人数")
    private Integer online;

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
}
