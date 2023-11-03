package com.cmnt.dbpick.stats.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("直播间统计数据")
public class LiveRoomStatsVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("累计播放流量")
    private Double sumFlux;

    @ApiModelProperty("最大在线人数")
    private Integer maxOnLine;

    @ApiModelProperty("最小在线人数")
    private Integer minOnLine;

    @ApiModelProperty("数据时间点，格式：yyyy-MM-dd HH:MM:SS")
    private String recordTime;

    @ApiModelProperty("操作时间戳")
    private Long recordTimeMillis;

    public Double getSumFlux() {
        if(Objects.isNull(sumFlux)){
            return 0.0d;
        }
        return sumFlux;
    }

    public Integer getMaxOnLine() {
        if(Objects.isNull(maxOnLine)){
            return 0;
        }
        return maxOnLine;
    }

    public Integer getMinOnLine() {
        if(Objects.isNull(minOnLine)){
            return 0;
        }
        return minOnLine;
    }
}
