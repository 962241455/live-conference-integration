package com.cmnt.dbpick.common.enums.live;

/**
 * 直播房间类型
 */
public enum RoomRecordedEnum {
    /**
     * 是否开启录制回放:
     * recorded     开启录制
     * unrecorded   不录制
     */

    UNKNOWN(-1, "unknown", "未知类型"),
    RECORDED(1, "recorded","开启录制"),
    UNRECORDED(2, "unrecorded","不录制"),
    ;

    private int code;
    private String value;
    private String desc;

    RoomRecordedEnum(int code, String value, String desc) {
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


    public static RoomRecordedEnum getByCode(int code) {
        RoomRecordedEnum statusEnum = UNKNOWN;
        RoomRecordedEnum[] values = RoomRecordedEnum.values();
        for (RoomRecordedEnum anEnum : values) {
            if (anEnum.getCode() == code) {
                statusEnum =  anEnum;
                break;
            }
        }
        return statusEnum;
    }
}
