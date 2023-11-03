package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import com.cmnt.dbpick.live.api.vo.RoomVideoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 直播间回放视频
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "streaming_room_playback_video")
@TypeAlias("streaming_room_playback_video")
public class StreamingRoomPlaybackVideo extends BaseDocument {

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

    @ApiModelProperty("播放顺序")
    private Integer playSort;

    @ApiModelProperty("播放视频名称")
    private String playFileName;


}
