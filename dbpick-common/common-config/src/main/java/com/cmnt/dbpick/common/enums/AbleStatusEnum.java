package com.cmnt.dbpick.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 可用状态枚举
 *
 * @author
 */
public enum AbleStatusEnum {

    /** 禁用(无效、下架等...) */
    DISABLE("disable", "禁用"),

    /** 启用(有效、上架等...) */
    ENABLE("enable","启用"),

    /** 删除 */
    DELETE("delete","删除"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    AbleStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AbleStatusEnum getByValue(String value) {
        AbleStatusEnum ableStatusEnum = UNKNOWN;
        AbleStatusEnum[] values = AbleStatusEnum.values();
        for (AbleStatusEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
