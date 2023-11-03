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
@ApiModel("房间 trtc 通话分钟数据")
@Document(indexName = "room_trtc_usage_index")
public class RoomTrtcUsageIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @ApiModelProperty("房间号")
    @Field(type = FieldType.Keyword)
    private String roomNo;

    @ApiModelProperty("通话ID（唯一标识一次通话）")
    @Field(type = FieldType.Keyword)
    private String commId;

    @ApiModelProperty("房间创建时间")
    @Field(type = FieldType.Integer)
    private Integer roomCreateTime;
    @ApiModelProperty("房间销毁时间")
    @Field(type = FieldType.Integer)
    private Integer roomDestroyTime;

    @ApiModelProperty("房间是否已经结束")
    @Field(type = FieldType.Keyword)
    private Boolean isFinished;

    @ApiModelProperty("房间创建者Id")
    @Field(type = FieldType.Keyword)
    private String userId;


    @ApiModelProperty("操作时间戳")
    @Field(type = FieldType.Keyword)
    private Long actTimeMillis;

}
