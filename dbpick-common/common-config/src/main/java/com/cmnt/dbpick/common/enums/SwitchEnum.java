package com.cmnt.dbpick.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 开关枚举
 * switch_close-关闭； switch_open-开启
 */
public enum SwitchEnum {

    CLOSE("switch_close", "关闭"),
    OPEN("switch_open", "开启"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    SwitchEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static SwitchEnum getByValue(String value) {
        SwitchEnum ableStatusEnum = UNKNOWN;
        SwitchEnum[] values = SwitchEnum.values();
        for (SwitchEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
