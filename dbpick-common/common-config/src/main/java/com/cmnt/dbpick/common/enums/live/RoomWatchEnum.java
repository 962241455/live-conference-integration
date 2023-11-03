package com.cmnt.dbpick.common.enums.live;

public enum RoomWatchEnum {

    /**
     * 类型：
     * 1、register  登记观看
     * 2、auth  验证码观看
     */

    WATCH_UNKNOWN(-1, "unknown", "未知类型"),
    WATCH_REGISTER(1, "register","登记观看"),
    WATCH_AUTH(2, "auth","验证码观看"),

    ;

    private int code;
    private String value;
    private String desc;

    RoomWatchEnum(int code, String value, String desc) {
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


    public static RoomWatchEnum getByCode(int code) {
        RoomWatchEnum watchEnum = WATCH_UNKNOWN;
        RoomWatchEnum[] values = RoomWatchEnum.values();
        for (RoomWatchEnum anEnum : values) {
            if (anEnum.getCode() == code) {
                watchEnum =  anEnum;
                break;
            }
        }
        return watchEnum;
    }
}
