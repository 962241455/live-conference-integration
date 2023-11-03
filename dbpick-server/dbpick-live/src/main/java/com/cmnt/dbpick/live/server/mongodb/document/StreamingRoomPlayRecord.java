package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 直播间播放记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "streaming_room_play_record")
@TypeAlias("streaming_room_play_record")
public class StreamingRoomPlayRecord extends BaseDocument {

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

    @ApiModelProperty("直播开始时间 yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty("直播停止时间 yyyy-MM-dd HH:mm:ss")
    private String stopTime;

    /**
     * @see com.cmnt.dbpick.common.enums.live.RoomPlayRecordStatusEnum
     */
    @Indexed
    @ApiModelProperty("直播记录状态: start-记录开始； stop-记录结束")
    private String status;

}
