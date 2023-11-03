package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 水印状态 枚举
 *   upload-上传完成, add_live-添加到直播
 */
public enum LiveWatermarkStatusEnum {

    UPLOAD("upload", "上传完成"),
    ADD_LIVE("add_live","添加到直播"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    LiveWatermarkStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static LiveWatermarkStatusEnum getByValue(String value) {
        LiveWatermarkStatusEnum ableStatusEnum = UNKNOWN;
        LiveWatermarkStatusEnum[] values = LiveWatermarkStatusEnum.values();
        for (LiveWatermarkStatusEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
