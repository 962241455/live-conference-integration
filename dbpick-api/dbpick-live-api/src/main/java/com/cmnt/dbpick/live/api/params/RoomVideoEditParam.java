package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@Data
@ApiModel(value = "直播间视频参数")
@AllArgsConstructor
@NoArgsConstructor
public class RoomVideoEditParam extends ThirdAccessKeyInfo {

    private String id;

    @ApiModelProperty("文件名")
    private String fileName;
    @ApiModelProperty("文件大小(字节)")
    private Long fileSize;

    @ApiModelProperty("视频地址")
    private String videoURL;
    @ApiModelProperty("源视频地址")
    private String originVideoURL;

    @ApiModelProperty("视频id")
    private String fileId;

    @ApiModelProperty("视频时长 HH:mm:ss")
    private String duration;

    @ApiModelProperty("视频时长 秒数")
    private Integer seconds;

    /**
     * @see com.cmnt.dbpick.common.enums.PullStreamStatusEnum
     */
    @ApiModelProperty("任务状态: enable - 启用, pause - 暂停。")
    private String status;

    @ApiModelProperty("（暖场视频）房间号")
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.tencent.TxVideoSourceType
     */
    @ApiModelProperty("视频来源：vod-音视频管理； cos-对象存储")
    private String source;

    /**
     * @see com.cmnt.dbpick.common.enums.live.VideoSourceTypeEnum
     */
    @ApiModelProperty(value = "视频来源方式： Upload-上传； Record-录制； TRtcPartnerRecord- TRTC伴生录制",hidden = true)
    private String sourceType;

    @ApiModelProperty("第一帧封面")
    private String cover;

    @ApiModelProperty("尺寸")
    private Integer width;
    private Integer height;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
