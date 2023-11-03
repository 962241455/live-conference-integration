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
@ApiModel("直播TRTC音视频总用量")
@Document(indexName = "live_trtc_usage_index")
public class LiveTrtcUsageIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @ApiModelProperty("数据时间点，格式：yyyy-MM-dd")
    @Field(type = FieldType.Keyword)
    private String recordTime;

    @ApiModelProperty("语音时长（分钟）")
    @Field(type = FieldType.Integer)
    private Integer Audio;

    @ApiModelProperty("标清时长（分钟）")
    @Field(type = FieldType.Integer)
    private Integer SD;

    @ApiModelProperty("高清时长（分钟）")
    @Field(type = FieldType.Integer)
    private Integer HD;

    @ApiModelProperty("超高清时长（分钟）")
    @Field(type = FieldType.Integer)
    private Integer FullHD;

    @ApiModelProperty("2K时长（分钟）")
    @Field(type = FieldType.Integer)
    private Integer video_2K;

    @ApiModelProperty("4K时长（分钟）")
    @Field(type = FieldType.Integer)
    private Integer video_4K;

}
