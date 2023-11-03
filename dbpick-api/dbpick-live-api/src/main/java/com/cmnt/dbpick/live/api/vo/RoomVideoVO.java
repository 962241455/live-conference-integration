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
@ApiModel("直播间视频")
public class RoomVideoVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;
    private String id;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件大小(字节)")
    private Long fileSize;

    @ApiModelProperty("文件大小(GB)")
    private String fileSizeGB;

    @ApiModelProperty("视频源地址(未转码)")
    private String originVideoURL;
    @ApiModelProperty("视频地址")
    private String videoURL;

    @ApiModelProperty("视频id")
    private String fileId;

    @ApiModelProperty("视频时长 秒数")
    private Integer seconds;

    /**
     * @see com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum
     */
    @ApiModelProperty("NORMAL-未转码； PROCESSING-转码中； FINISH- 转码完成；FAILED- 转码失败")
    private String transcodeStatus;

    @ApiModelProperty("视频时长 HH:mm:ss")
    private String duration;

    @ApiModelProperty("第一帧封面")
    private String cover;

    @ApiModelProperty("尺寸")
    private Integer width;
    private Integer height;

    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

    @ApiModelProperty("转码时间戳")
    private Long transcodeTime;

}
