package com.cmnt.dbpick.common.enums.live;

/**
 * 登记观看数据类型
 */
public enum InformationTypeEnum {

    /**
     * 类型：
     * 1、_txt  文本
     * 2、_name  姓名
     * 3、_digit  数字
     * 4、_select  下拉选择
     */
    _UNKNOWN(-1, "unknown", "未知类型"),
    _TXT(1, "_txt","文本"),
    _NAME(2, "_name","姓名"),
    _DIGIT(3, "_digit","数字"),
    _SELECT(4, "_select","下拉选择"),

    ;

    private int code;
    private String value;
    private String desc;

    InformationTypeEnum(int code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }


    public static InformationTypeEnum getByCode(int code) {
        InformationTypeEnum watchEnum = _UNKNOWN;
        InformationTypeEnum[] values = InformationTypeEnum.values();
        for (InformationTypeEnum anEnum : values) {
            if (anEnum.getCode() == code) {
                watchEnum =  anEnum;
                break;
            }
        }
        return watchEnum;
    }
}
