package com.cmnt.dbpick.live.api.vo.redis;

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
@ApiModel("直播断流房间")
public class LiveStreamStopVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("房间待处理状态")
    private String roomStatus;

    @ApiModelProperty("处理房间状态时间 毫秒")
    private Long handleRoomStatusTime;

}
