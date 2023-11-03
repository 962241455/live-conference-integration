package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 直播间观看限制
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "streaming_room_watch_filter")
@TypeAlias("streaming_room_watch_filter")
public class StreamingRoomWatchFilter extends BaseDocument {

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
     * @see com.cmnt.dbpick.common.enums.live.LiveWatchFilterTypeEnum
     */
    @Indexed
    @ApiModelProperty("限制类型：no_filter-没有限制； register_filter-登记观看")
    private String filterType;


    @ApiModelProperty("详细信息")
    private String filterInfoJson;



}
