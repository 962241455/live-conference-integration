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
@ApiModel("直播间数据统计记录")
@Document(indexName = "room_info_record_index")
public class RoomInfoRecordIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @ApiModelProperty("房间号")
    @Field(type = FieldType.Keyword)
    private String roomNo;

    @ApiModelProperty("累计播放流量")
    @Field(type = FieldType.Keyword)
    private Double sumFlux;

    @ApiModelProperty("最大在线人数")
    @Field(type = FieldType.Keyword)
    private Integer maxOnLine;

    @ApiModelProperty("最小在线人数")
    @Field(type = FieldType.Keyword)
    private Integer minOnLine;

    @ApiModelProperty("数据时间点，格式：yyyy-mm-dd HH:MM:SS")
    @Field(type = FieldType.Keyword)
    private String recordTime;

    @ApiModelProperty("操作时间戳")
    @Field(type = FieldType.Keyword)
    private Long recordTimeMillis;

}
