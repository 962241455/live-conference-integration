package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 视频转码状态
 * NORMAL-未转码； PROCESSING-转码中； FINISH- 转码完成；FAILED- 转码失败
 */
public enum VideoTranscodeStatusEnum {

    NORMAL("NORMAL", "未转码"),
    PROCESSING("PROCESSING","转码中"),
    FINISH("FINISH","转码完成"),
    FAILED("FAILED","转码失败"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    VideoTranscodeStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static VideoTranscodeStatusEnum getByValue(String value) {
        VideoTranscodeStatusEnum ableStatusEnum = UNKNOWN;
        VideoTranscodeStatusEnum[] values = VideoTranscodeStatusEnum.values();
        for (VideoTranscodeStatusEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
