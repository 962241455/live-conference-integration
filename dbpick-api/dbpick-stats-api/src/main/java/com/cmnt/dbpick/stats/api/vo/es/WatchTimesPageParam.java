package com.cmnt.dbpick.stats.api.vo.es;

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
@ApiModel("查询观看时长参数")
public class WatchTimesPageParam implements Serializable {

    private static final long serialVersionUID = 101673413147308908L;

    @ApiModelProperty("最大页数")
    private Integer maxPage;

    @ApiModelProperty("每页条数")
    private Integer pageSize;

    @ApiModelProperty("用户单次出入房间统计索引")
    private String userRecordOnceIdx;

    @ApiModelProperty("用户进入房间累计时长统计索引")
    private String userRecordStatsIdx;

    @ApiModelProperty("用户出入房间行为记录索引")
    private String userRecordIdx;

}
