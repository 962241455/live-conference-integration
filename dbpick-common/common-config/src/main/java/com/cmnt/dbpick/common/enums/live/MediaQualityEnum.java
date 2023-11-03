package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 媒体质量 枚举
 * media_sd-标清, media_hd-高清, media_fhd-超高清, media_2k-2k, media_4k-4k
 */
public enum MediaQualityEnum {


    MEDIA_SD("media_sd", 480,1,"标清"),
    MEDIA_HD("media_hd", 720,2,"高清"),
    MEDIA_FHD("media_fhd",1080,4, "超高清"),
    MEDIA_2K("media_2k", 1440,8,"2k"),
    MEDIA_4K("media_4k",2160,16,"4k"),

    UNKNOWN("unknown",0,1,"未知画质");

    private String value;
    private Integer standard;
    private Integer ratio;
    private String desc;

    MediaQualityEnum(String value, Integer standard, Integer ratio, String desc) {
        this.value = value;
        this.standard = standard;
        this.ratio = ratio;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public Integer getStandard() {
        return standard;
    }

    public Integer getRatio() {
        return ratio;
    }

    public String getDesc() {
        return desc;
    }

    public static MediaQualityEnum getByValue(String value) {
        MediaQualityEnum ableStatusEnum = UNKNOWN;
        MediaQualityEnum[] values = MediaQualityEnum.values();
        for (MediaQualityEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }


    /**
     * 获取画质
     * media_sd-标清, media_hd-高清, media_fhd-超高清, media_2k-2k, media_4k-4k
     */
    public static MediaQualityEnum getMediaQualityByWH(Integer width,Integer height){
        if(Objects.isNull(width) || Objects.isNull(height)){
            return UNKNOWN;
        }
        Integer minQuality = new ArrayList<Integer>(){{
            add(width);add(height);
        }}.stream().reduce(Integer::min).get();
        if(minQuality <= MEDIA_SD.getStandard()) {
            return MEDIA_SD;
        }else if(minQuality <= MEDIA_HD.getStandard()) {
            return MEDIA_HD;
        }else if(minQuality <= MEDIA_FHD.getStandard()) {
            return MEDIA_FHD;
        }else if(minQuality <= MEDIA_2K.getStandard()) {
            return MEDIA_2K;
        }else if(minQuality <= MEDIA_4K.getStandard()) {
            return MEDIA_4K;
        }else {
            return UNKNOWN;
        }
    }

}
