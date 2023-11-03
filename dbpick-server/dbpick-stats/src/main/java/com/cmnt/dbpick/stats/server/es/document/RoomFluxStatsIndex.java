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
@ApiModel("直播间每日流量统计")
@Document(indexName = "room_flux_stats_index")
public class RoomFluxStatsIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @ApiModelProperty("房间号")
    @Field(type = FieldType.Keyword)
    private String roomNo;

    @ApiModelProperty("总流量，单位: MB。")
    @Field(type = FieldType.Float)
    private Float totalFlux;


    @ApiModelProperty("数据时间点，格式：yyyy-MM-dd")
    @Field(type = FieldType.Keyword)
    private String recordTime;

    @ApiModelProperty("操作时间戳")
    @Field(type = FieldType.Keyword)
    private Long recordTimeMillis;

    @ApiModelProperty("商户id")
    @Field(type = FieldType.Keyword)
    private String thirdId;

}
