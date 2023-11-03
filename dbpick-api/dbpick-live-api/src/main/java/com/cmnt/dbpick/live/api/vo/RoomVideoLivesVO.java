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
@ApiModel("直播视频")
public class RoomVideoLivesVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;
    private String id;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("文件id")
    private String fileId;
    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("播放地址")
    private String playUrl;

    /**
     * @see com.cmnt.dbpick.common.enums.PullStreamStatusEnum
     */
    @ApiModelProperty("任务状态: enable - 启用, pause - 暂停。")
    private String status;

    @ApiModelProperty("视频时长 HH:mm:ss")
    private String duration;

    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

}
