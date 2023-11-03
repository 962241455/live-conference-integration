package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 录播视频库
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "live_videos")
@TypeAlias("live_videos")
public class LiveVideos extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @Indexed
    @ApiModelProperty("视频id")
    private String fileId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件大小(字节)")
    private Long fileSize;

    @ApiModelProperty("视频源地址(未转码)")
    private String originVideoURL;

    @ApiModelProperty("视频播放地址")
    private String videoURL;

    @ApiModelProperty("视频时长 HH:mm:ss")
    private String duration;

    @ApiModelProperty("视频时长 秒数")
    private Integer seconds;

    @ApiModelProperty("第一帧封面")
    private String cover;

    @ApiModelProperty("尺寸")
    private Integer width;
    private Integer height;


    /**
     * @see com.cmnt.dbpick.common.enums.tencent.TxVideoSourceType
     */
    @ApiModelProperty("视频存储：vod-音视频管理； cos-对象存储")
    private String source;

    /**
     * @see com.cmnt.dbpick.common.enums.live.VideoSourceTypeEnum
     */
    @ApiModelProperty("视频来源方式： Upload-上传； Record-录制； TRtcPartnerRecord- TRTC伴生录制")
    private String sourceType;

    @ApiModelProperty("响应id")
    private String txRequestId;

    /**
     * @see com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum
     */
    @ApiModelProperty("NORMAL-未转码； PROCESSING-转码中； FINISH- 转码完成；FAILED- 转码失败")
    private String transcodeStatus;

    @ApiModelProperty("转码任务")
    private String txTranscodeTaskId;


}
