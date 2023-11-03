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
@ApiModel("直播间回放视频")
public class RoomPlaybackVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;
    private String id;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("视频源地址(未转码)")
    private String originVideoURL;
    @ApiModelProperty("视频地址")
    private String videoURL;

    @ApiModelProperty("第一帧封面")
    private String cover;

    @ApiModelProperty("尺寸")
    private Integer width;
    private Integer height;

    @ApiModelProperty("视频id")
    private String fileId;

    @ApiModelProperty("视频时长 秒数")
    private Integer seconds;

    @ApiModelProperty("视频时长 HH:mm:ss")
    private String duration;

    @ApiModelProperty("播放顺序")
    private Integer playSort;

    @ApiModelProperty("播放视频名称")
    private String playFileName;

    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

}
