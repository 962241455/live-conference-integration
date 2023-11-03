package com.cmnt.dbpick.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 拉流源的类型 枚举
 *
 * @author
 */
public enum PullStreamSourceTypeEnum {

    /** 直播 */
    PULL_LIVE("PullLivePushLive", "直播"),

    /** 点播 */
    PULL_VOD("PullVodPushLive","点播"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    PullStreamSourceTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static PullStreamSourceTypeEnum getByValue(String value) {
        PullStreamSourceTypeEnum ableStatusEnum = UNKNOWN;
        PullStreamSourceTypeEnum[] values = PullStreamSourceTypeEnum.values();
        for (PullStreamSourceTypeEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
