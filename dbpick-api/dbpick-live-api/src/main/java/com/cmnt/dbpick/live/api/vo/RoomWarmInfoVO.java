package com.cmnt.dbpick.live.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shusong.liang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("直播间暖场视频")
public class RoomWarmInfoVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;
    private String id;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("视频id")
    private String fileId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("视频地址")
    private String videoURL;

    @ApiModelProperty("视频时长 HH:mm:ss")
    private String duration;

    @ApiModelProperty("开始类型： immediately - 立刻开始")
    private String startType;


    @ApiModelProperty("开播时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * @see com.cmnt.dbpick.common.enums.PullStreamStatusEnum
     */
    @ApiModelProperty("任务状态: enable - 开启, pause - 关闭。")
    private String status;

    @ApiModelProperty("播放地址")
    private String playUrl;

}
