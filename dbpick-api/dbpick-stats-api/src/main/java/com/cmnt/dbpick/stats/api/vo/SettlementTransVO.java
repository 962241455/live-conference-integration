package com.cmnt.dbpick.stats.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("直播结算-视频转码对象")
public class SettlementTransVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("转码总时长，单位: 分钟。")
    private Long transDuration;

    @ApiModelProperty("转码视频列表")
    private List<DetailVO> transVideoList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailVO{

        @ApiModelProperty("视频id")
        private String fileId;

        @ApiModelProperty("视频名称")
        private String fileName;

        @ApiModelProperty("视频时长 HH:mm:ss")
        private String duration;

        @ApiModelProperty("视频时长 秒数")
        private Integer seconds;

        @ApiModelProperty("尺寸")
        private Integer width;
        private Integer height;

        /**
         * @see com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum
         */
        @ApiModelProperty("转码状态: NORMAL-未转码； PROCESSING-转码中； FINISH- 转码完成；FAILED- 转码失败")
        private String transcodeStatus;

        @ApiModelProperty("计费时长(分钟) = 转码时长(视频时长)*画质")
        private Long costSeconds;

        /**
         * @see com.cmnt.dbpick.common.enums.live.MediaQualityEnum
         */
        @ApiModelProperty("画质: media_sd-标清, media_hd-高清, media_fhd-超高清, media_2k-2k, media_4k-4k")
        private String fileQuality;

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

        @ApiModelProperty("创建时间戳")
        private Long createDateTime;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }

    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
