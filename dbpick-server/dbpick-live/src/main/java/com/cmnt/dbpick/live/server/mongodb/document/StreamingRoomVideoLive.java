package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import com.cmnt.dbpick.live.api.vo.RoomVideoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 视频直播
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "streaming_room_video_live")
@TypeAlias("streaming_room_video_live")
public class StreamingRoomVideoLive extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @Indexed
    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("视频信息")
    private RoomVideoVO videoInfo;

    @ApiModelProperty("开播时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * @see com.cmnt.dbpick.common.enums.PullStreamStatusEnum
     */
    @ApiModelProperty("任务状态: enable - 启用, pause - 暂停。")
    private String status;

    @ApiModelProperty("播放地址")
    private String playUrl;

    @ApiModelProperty("视频时长 HH:mm:ss")
    private String duration;

    @Indexed
    @ApiModelProperty("任务id")
    private String txTaskId;

    @ApiModelProperty("响应id")
    private String txRequestId;

}
