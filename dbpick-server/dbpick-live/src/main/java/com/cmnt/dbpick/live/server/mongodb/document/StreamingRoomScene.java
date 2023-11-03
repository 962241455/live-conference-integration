package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import com.cmnt.dbpick.live.api.vo.AppendixInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * 直播现场
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "streaming_room_scene")
@TypeAlias("streaming_room_scene")
public class StreamingRoomScene extends BaseDocument {

    @Indexed
    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("信息描述")
    private String msgDesc;

    @ApiModelProperty("过期时间")
    private String overTime;


    @ApiModelProperty("附件信息")
    private Set<AppendixInfoVO> appendixList;


    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @Indexed
    @ApiModelProperty("现场开关：switch_close-下架； switch_open-上架")
    private String statusSwitch;


}
