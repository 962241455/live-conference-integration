package com.cmnt.dbpick.live.api.vo.redis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("直播录制任务")
public class LiveRecordTaskVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("直播间状态： live_ing-直播中，录制中， 其他-录制结束")
    private String roomStatus;

    @ApiModelProperty("录制任务id")
    private String taskId;

    @ApiModelProperty("录制结束时间 毫秒")
    private Long taskEndTime;


    @ApiModelProperty("录制停止时间 毫秒")
    private Long taskStopTime;

}
