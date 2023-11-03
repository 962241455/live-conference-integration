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
@ApiModel("直播结算-回放流量对象")
public class SettlementPlaybackVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("回放总流量，单位: GB。")
    private Long sumPlaybackFlux;

    @ApiModelProperty("转码视频列表")
    private List<DetailVO> playbackFluxList;

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

        @ApiModelProperty("回放次数")
        private Long playTimes;
        @ApiModelProperty("总流量，单位: MB。")
        private Long playFlux;
        @ApiModelProperty("总流量，单位: GB。")
        private Long playFluxGB;

        /**
         * @see com.cmnt.dbpick.common.enums.live.VideoTranscodeStatusEnum
         */
        @ApiModelProperty("转码状态: NORMAL-未转码； PROCESSING-转码中； FINISH- 转码完成；FAILED- 转码失败")
        private String transcodeStatus;

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
