package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 音视频来源 枚举
 * Upload-上传； Record-录制； TRtcPartnerRecord- TRTC伴生录制
 */
public enum VideoSourceTypeEnum {

    UPLOAD("Upload", "上传"),
    LIVE_RECORD("Record","直播录制"),
    TRTC_RECORD("TRtcPartnerRecord","TRTC伴生录制"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    VideoSourceTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static VideoSourceTypeEnum getByValue(String value) {
        VideoSourceTypeEnum ableStatusEnum = UNKNOWN;
        VideoSourceTypeEnum[] values = VideoSourceTypeEnum.values();
        for (VideoSourceTypeEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
