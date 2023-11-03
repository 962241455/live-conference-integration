package com.cmnt.dbpick.stats.api.vo;

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
@ApiModel("用户观看直播统计数据")
public class LiveUserStatsVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("用户角色：主播、观众")
    private String userRole;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;
    @ApiModelProperty("首次进入房间时间")
    private String firstWatchTime;

    @ApiModelProperty("观看时长(单位 s)")
    private Long watchSeconds;

    @ApiModelProperty("观看次数")
    private Long watchTimes;


    @ApiModelProperty("第一次签到时间")
    private String firstSignTime;

    @ApiModelProperty("最后一次签到时间")
    private String lastSignTime;

    @ApiModelProperty("签到次数")
    private Long signTimes;


}
