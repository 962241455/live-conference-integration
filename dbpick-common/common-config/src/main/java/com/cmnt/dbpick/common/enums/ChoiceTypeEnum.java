package com.cmnt.dbpick.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 选择类型枚举
 * radio_type-单选 checkbox_type-多选
 */
public enum ChoiceTypeEnum {

    RADIO("radio_type", "单选"),
    CHECKBOX("checkbox_type","多选"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    ChoiceTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ChoiceTypeEnum getByValue(String value) {
        ChoiceTypeEnum ableStatusEnum = UNKNOWN;
        ChoiceTypeEnum[] values = ChoiceTypeEnum.values();
        for (ChoiceTypeEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
