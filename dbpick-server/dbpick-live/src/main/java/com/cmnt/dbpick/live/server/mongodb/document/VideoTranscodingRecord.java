package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * 视频转码记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "video_transcoding_record")
@TypeAlias("video_transcoding_record")
public class VideoTranscodingRecord extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @Indexed
    @ApiModelProperty("视频id")
    private String fileId;

    @ApiModelProperty("视频名称")
    private String fileName;

    @ApiModelProperty("视频大小(字节)")
    private Long fileSize;

    @ApiModelProperty("视频源地址")
    private String originVideoURL;

    @ApiModelProperty("视频转码完成地址")
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

    @ApiModelProperty("转码日期(转码开始时间,格式:yyyy-MM-dd)")
    private String transDate;

    /**
     * @see com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum
     */
    @ApiModelProperty("转码状态：FINISH- 转码成功；FAILED- 转码失败")
    private String transStatus;

    @ApiModelProperty("转码用时(毫秒)")
    private Long transDuration;

    @ApiModelProperty("视频被使用的房间号")
    private Set<String> usedRoomNos;


}
