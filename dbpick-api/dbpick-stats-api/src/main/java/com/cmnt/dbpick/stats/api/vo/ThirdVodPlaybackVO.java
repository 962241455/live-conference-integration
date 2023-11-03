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

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("直播结算-vod视频回放对象")
public class ThirdVodPlaybackVO implements Serializable {

    @ApiModelProperty("播放日期")
    private String date;
    @ApiModelProperty("视频文件 ID")
    private String fileId;
    @ApiModelProperty("去重后的客户端 IP 数")
    private Long ipCount;
    @ApiModelProperty("总的播放次数")
    private Long playTimes;
    @ApiModelProperty("播放流量，单位：字节")
    private Long flux;
    @ApiModelProperty("PC 端播放次数")
    private Long pcPlayTimes;
    @ApiModelProperty("移动端播放次数")
    private Long mobilePlayTimes;
    @ApiModelProperty("iPhone 端播放次数")
    private Long iphonePlayTimes;
    @ApiModelProperty("Android 端播放次数")
    private Long androidPlayTimes;
    @ApiModelProperty("域名")
    private String hostName;


    @ApiModelProperty("商户账号")
    private String thirdId;
    @ApiModelProperty("文件名")
    private String fileName;
    @ApiModelProperty("视频时长 HH:mm:ss")
    private String duration;
    @ApiModelProperty("尺寸")
    private Integer width;
    private Integer height;
    /**
     * @see com.cmnt.dbpick.common.enums.live.MediaQualityEnum
     */
    @ApiModelProperty("画质: media_sd-标清, media_hd-高清, media_fhd-超高清, media_2k-2k, media_4k-4k")
    private String fileQuality;

    @ApiModelProperty(value = "是否在结算日期之中：in_tm-在; out_tm-不在",hidden = true)
    private String inCountTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
