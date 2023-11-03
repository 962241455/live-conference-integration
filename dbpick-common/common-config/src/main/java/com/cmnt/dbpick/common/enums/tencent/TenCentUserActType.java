package com.cmnt.dbpick.common.enums.tencent;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 用户行为， 103 ：进入房间， 104：退出房间
 */
public enum TenCentUserActType {

    ENTER_ROOM(103,"enter"),
    EXIT_ROOM(104,"exit"),


    UNKNOWN(999,"unknown")
    ;

    private int code;

    private String value;

    TenCentUserActType(int code, String value) {
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
        for (TenCentUserActType channel : values()) {
            if (channel.code == code) {
                return channel.getValue();
            }
        }
        return String.valueOf(code);
    }

    /**
     * find enum by channel tag.
     */
    public static TenCentUserActType lookup(String value) {
        TenCentUserActType type = UNKNOWN;
        TenCentUserActType[] values = TenCentUserActType.values();
        for (TenCentUserActType anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                type =  anEnum;
                break;
            }
        }
        return type;
    }

}
