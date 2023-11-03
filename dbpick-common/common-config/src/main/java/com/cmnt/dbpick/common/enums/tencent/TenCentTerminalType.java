package com.cmnt.dbpick.common.enums.tencent;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 终端类型， 1 ：windows， 2 ：android， 3 ：ios， 4 ：linux， 100 ：other
 */
public enum TenCentTerminalType {

    WINDOWS(1,"windows"),
    ANDROID(2,"android"),
    IOS(3,"ios"),
    LINUX(4,"linux"),
    OTHER(100,"other")

    ;

    private int code;

    private String value;

    TenCentTerminalType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    /**
     * find enum by channel tag.
     */
    public static String lookup(int code) {
        for (TenCentTerminalType channel : values()) {
            if (channel.code == code) {
                return channel.getValue();
            }
        }
        return String.valueOf(code);
    }

    public static TenCentTerminalType lookup(String value) {
        TenCentTerminalType type = OTHER;
        TenCentTerminalType[] values = TenCentTerminalType.values();
        for (TenCentTerminalType anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                type =  anEnum;
                break;
            }
        }
        return type;
    }

}
