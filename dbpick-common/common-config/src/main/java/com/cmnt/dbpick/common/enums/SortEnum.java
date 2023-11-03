package com.cmnt.dbpick.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 排序枚举
 * upward-向上； downward-向下
 */
public enum SortEnum {

    UPWARD("upward", "向上"),
    DOWNWARD("downward", "向下"),

    ASC("asc", "升序"),
    DESC("desc", "降序"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    SortEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static SortEnum getByValue(String value) {
        SortEnum ableStatusEnum = UNKNOWN;
        SortEnum[] values = SortEnum.values();
        for (SortEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
