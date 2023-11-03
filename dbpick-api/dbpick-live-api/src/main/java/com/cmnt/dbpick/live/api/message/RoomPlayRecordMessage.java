package com.cmnt.dbpick.live.api.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("房间播放记录mq消息")
public class RoomPlayRecordMessage implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("直播开始时间 yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty("直播停止时间 yyyy-MM-dd HH:mm:ss")
    private String stopTime;

}
