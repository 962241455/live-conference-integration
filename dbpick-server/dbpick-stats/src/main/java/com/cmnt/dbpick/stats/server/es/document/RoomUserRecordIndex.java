package com.cmnt.dbpick.stats.server.es.document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("直播间用户行为记录")
@Document(indexName = "room_user_record_index")
public class RoomUserRecordIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @ApiModelProperty("用户id")
    @Field(type = FieldType.Keyword)
    private String userId;

    @ApiModelProperty("房间号")
    @Field(type = FieldType.Keyword)
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum
     */
    @ApiModelProperty("观看类型: live-直播； playback-回放")
    @Field(type = FieldType.Auto)
    private String watchType;

    @ApiModelProperty("商户")
    @Field(type = FieldType.Keyword)
    private String thirdId;

    @ApiModelProperty("第三方用户id")
    @Field(type = FieldType.Keyword)
    private String thirdUserId;

    /**
     * @see com.cmnt.dbpick.common.enums.UserRoleEnum
     */
    @ApiModelProperty("用户角色 startv:主播 | watch:观众 | major|管理")
    @Field(type = FieldType.Auto)
    private String userRole;

    /**
     * @see com.cmnt.dbpick.common.enums.tencent.TxUserActType
     */
    @ApiModelProperty("行为方式：enter-进入房间； exit-退出房间")
    @Field(type = FieldType.Keyword)
    private String actType;

    @ApiModelProperty("操作时间戳")
    @Field(type = FieldType.Keyword)
    private Long actTimeMillis;

    @ApiModelProperty("操作时间 yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Auto)
    private String actTime;

    /**
     * @see com.cmnt.dbpick.common.enums.tencent.TxTerminalType
     */
    @ApiModelProperty("终端类型: 1-Windows 端; 2-Android 端; 3-iOS 端; 4-Linux 端; 100-其他")
    @Field(type = FieldType.Auto)
    private String terminalType;

    @ApiModelProperty("ip地址")
    @Field(type = FieldType.Auto)
    private String ip;

    @ApiModelProperty("地域")
    @Field(type = FieldType.Auto)
    private String area;

}
