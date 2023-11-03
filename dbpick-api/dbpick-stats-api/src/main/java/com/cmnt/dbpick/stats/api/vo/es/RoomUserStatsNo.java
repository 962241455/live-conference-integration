package com.cmnt.dbpick.stats.api.vo.es;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户观看时长统计(房间号)")
public class RoomUserStatsNo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("商户")
    private String thirdId;

    @ApiModelProperty("第三方用户id")
    private String thirdUserId;


    private Long watchTotalTimes;

    private String watchTotalTimesStr;


}
