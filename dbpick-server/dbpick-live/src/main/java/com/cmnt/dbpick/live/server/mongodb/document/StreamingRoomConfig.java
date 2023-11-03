package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 直播间通用配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "streaming_room_config")
@TypeAlias("streaming_room_config")
public class StreamingRoomConfig extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @ApiModelProperty("房间标题")
    private String title;

    @ApiModelProperty("房间logo封面")
    private String logoCover;

    @ApiModelProperty("房间背景图")
    private String bgImg;

    @ApiModelProperty("默认主播头像")
    private String streamerAvatar;

    @ApiModelProperty("房间介绍")
    private String announcement;


}
