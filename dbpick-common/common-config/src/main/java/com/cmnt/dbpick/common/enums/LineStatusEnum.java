package com.cmnt.dbpick.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 在线状态枚举
 *
 * @author
 */
public enum LineStatusEnum {

    /** 离线 */
    OFFLINE("offline", "离线"),

    /** 在线 */
    ONLINE("online","在线"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    LineStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static LineStatusEnum getByValue(String value) {
        LineStatusEnum ableStatusEnum = UNKNOWN;
        LineStatusEnum[] values = LineStatusEnum.values();
        for (LineStatusEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
