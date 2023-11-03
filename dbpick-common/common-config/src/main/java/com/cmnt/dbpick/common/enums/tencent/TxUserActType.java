package com.cmnt.dbpick.common.enums.tencent;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户行为， 103 ：进入房间， 104：退出房间
 */
public enum TxUserActType {

    ENTER_ROOM(103,"enter"),
    EXIT_ROOM(104,"exit"),


    UNKNOWN(999,"unknown")
    ;

    private int code;

    private String value;

    TxUserActType(int code, String value) {
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
        for (TxUserActType channel : values()) {
            if (channel.code == code) {
                return channel.getValue();
            }
        }
        return String.valueOf(code);
    }

    /**
     * find enum by channel tag.
     */
    public static TxUserActType lookup(String value) {
        TxUserActType type = UNKNOWN;
        TxUserActType[] values = TxUserActType.values();
        for (TxUserActType anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                type =  anEnum;
                break;
            }
        }
        return type;
    }

}
