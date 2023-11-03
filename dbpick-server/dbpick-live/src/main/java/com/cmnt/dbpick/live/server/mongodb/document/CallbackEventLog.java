package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * trtc 回调事件日志
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "callback_event_log")
@TypeAlias("callback_event_log")
public class CallbackEventLog extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @ApiModelProperty("事件来源: trtc-实时音视频, live-云直播, vod-云点播")
    private String EventSource;

    @ApiModelProperty("事件组 ID: 1-房间事件组, 2-媒体事件组, 3-云端录制事件组")
    private Integer EventGroupId;

    @ApiModelProperty("回调通知的事件类型: 参考各组")
    private String EventType;

    @ApiModelProperty("回调请求的 Unix 时间戳，单位为毫秒")
    private Long CallbackTs;

    @ApiModelProperty("房间号")
    private String RoomId;

    @ApiModelProperty("事件发生的 Unix 时间戳，单位为秒")
    private Long EventTs;

    @ApiModelProperty("用户 ID")
    private String UserId;


    @ApiModelProperty("具体事件信息")
    private Object EventInfo;


}
