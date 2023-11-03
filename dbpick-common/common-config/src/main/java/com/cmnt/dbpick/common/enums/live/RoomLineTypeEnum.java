package com.cmnt.dbpick.common.enums.live;

/**
 * 直播延迟类型
 */
public enum RoomLineTypeEnum {
    /**
     * 直播延迟类型:
     * leb 快直播; cdn 标准直播
     */

    UNKNOWN(-1, "unknown", "未知类型"),
    LEB(1, "leb","快直播"),
    CDN(2, "cdn","标准直播"),
    ;

    private int code;
    private String value;
    private String desc;

    RoomLineTypeEnum(int code, String value, String desc) {
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


    public static RoomLineTypeEnum getByCode(int code) {
        RoomLineTypeEnum statusEnum = UNKNOWN;
        RoomLineTypeEnum[] values = RoomLineTypeEnum.values();
        for (RoomLineTypeEnum anEnum : values) {
            if (anEnum.getCode() == code) {
                statusEnum =  anEnum;
                break;
            }
        }
        return statusEnum;
    }
}
