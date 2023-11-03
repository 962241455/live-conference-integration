package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

/**
 * 直播房间类型
 */
public enum RoomTypeEnum {
    /**
     * 类型：
     * 1、meeting 多人会议
     * 2、live    单人直播
     * 3、record  录播
     * 4、third_push  三方推流
     */

    UNKNOWN(-1, "unknown", "未知类型"),
    MEETING(1, "meeting","多人会议"),
    LIVE(2, "live","单人直播"),
    RECORD(3, "record", "录播"),
    THIRD_PUSH(4, "third_push", "三方推流")
    ;

    private int code;
    private String value;
    private String desc;

    RoomTypeEnum(int code, String value, String desc) {
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


    public static RoomTypeEnum getByCode(int code) {
        RoomTypeEnum statusEnum = UNKNOWN;
        RoomTypeEnum[] values = RoomTypeEnum.values();
        for (RoomTypeEnum anEnum : values) {
            if (anEnum.getCode() == code) {
                statusEnum =  anEnum;
                break;
            }
        }
        return statusEnum;
    }


    public static RoomTypeEnum getByValue(String value) {
        RoomTypeEnum typeEnum = UNKNOWN;
        RoomTypeEnum[] values = RoomTypeEnum.values();
        for (RoomTypeEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                typeEnum =  anEnum;
                break;
            }
        }
        return typeEnum;
    }
}
