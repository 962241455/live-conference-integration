package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 登记观看 字段名 枚举
 * registerName-登记姓名； registerPhone-登记手机号
 */
public enum WatchFilterFiledEnum {

    REGISTER_NAME("registerName", "登记姓名"),
    REGISTER_PHONE("registerPhone", "登记手机号"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    WatchFilterFiledEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static WatchFilterFiledEnum getByValue(String value) {
        WatchFilterFiledEnum ableStatusEnum = UNKNOWN;
        WatchFilterFiledEnum[] values = WatchFilterFiledEnum.values();
        for (WatchFilterFiledEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
