package com.cmnt.dbpick.live.api.vo;

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
@ApiModel("直播间热度在线人数参数")
public class RoomHotOnlineVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("热度值")
    private Integer hotScores;

    @ApiModelProperty("在线数")
    private Integer online;


}
