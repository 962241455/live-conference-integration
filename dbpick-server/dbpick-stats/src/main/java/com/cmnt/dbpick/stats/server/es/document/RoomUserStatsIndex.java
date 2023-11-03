//package com.cmnt.dbpick.stats.server.es.document;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//@Builder
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@ApiModel("直播间用户行为统计")
//@Document(indexName = "room_user_stats_index")
//public class RoomUserStatsIndex implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @Field(type = FieldType.Keyword)
//    private String id;
//
//    @ApiModelProperty("用户id")
//    @Field(type = FieldType.Keyword)
//    private String userId;
//
//    @ApiModelProperty("房间号")
//    @Field(type = FieldType.Keyword)
//    private String roomNo;
//
//    @ApiModelProperty("商户")
//    @Field(type = FieldType.Keyword)
//    private String thirdId;
//
//    @ApiModelProperty("第三方用户id")
//    @Field(type = FieldType.Keyword)
//    private String thirdUserId;
//
//    /**
//     * @see com.cmnt.dbpick.common.enums.UserRoleEnum
//     */
//    @ApiModelProperty("用户角色 startv:主播 | watch:观众 | major|管理")
//    @Field(type = FieldType.Auto)
//    private String userRole;
//
//    @ApiModelProperty("首次进入房间时间")
//    private String firstWatchTime;
//
//    @ApiModelProperty("观看时长(单位 s)")
//    @Field(type = FieldType.Keyword)
//    private Long watchSeconds;
//
//    @ApiModelProperty("观看次数")
//    @Field(type = FieldType.Keyword)
//    private Long watchTimes;
//
//
//    @ApiModelProperty("第一次签到时间")
//    @Field(type = FieldType.Keyword)
//    private String firstSignTime;
//
//    @ApiModelProperty("最后一次签到时间")
//    @Field(type = FieldType.Keyword)
//    private String lastSignTime;
//
//    @ApiModelProperty("签到次数")
//    @Field(type = FieldType.Keyword)
//    private Long signTimes;
//
//
//    @ApiModelProperty("操作时间戳")
//    @Field(type = FieldType.Keyword)
//    private Long actTimeMillis;
//
//    @ApiModelProperty("操作时间 yyyy-MM-dd HH:mm:ss")
//    @Field(type = FieldType.Auto)
//    private String actTime;
//
//
//}
