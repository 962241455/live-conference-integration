package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

/**
 * 直播房间状态枚举
 *
 * @author
 */
public enum RoomStatusEnum {
    /**
     * 状态：
     * 0 no_live   未开播
     * 1 live_ing  直播中
     * 2 forbidden 房间封禁
     * 3 live_pause 直播暂停
     * 4 live_over 直播结束
     */
    UNKNOWN(-1, "unknown", "未知类型"),
    NO_LIVE(0, "no_live","未开播"),
    LIVE_ING(1, "live_ing","直播中"),
    FORBIDDEN(2, "forbidden","房间封禁"),
    LIVE_PAUSE(3, "live_pause","直播暂停"),
    LIVE_OVER(4, "live_over","直播结束"),
    ;

    private int code;
    private String value;
    private String desc;

    RoomStatusEnum(int code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static RoomStatusEnum getByCode(int code) {
        RoomStatusEnum statusEnum = UNKNOWN;
        RoomStatusEnum[] values = RoomStatusEnum.values();
        for (RoomStatusEnum anEnum : values) {
            if (anEnum.getCode() == code) {
                statusEnum =  anEnum;
                break;
            }
        }
        return statusEnum;
    }


    public static RoomStatusEnum lookup(String value) {
        RoomStatusEnum statusEnum = UNKNOWN;
        RoomStatusEnum[] values = RoomStatusEnum.values();
        for (RoomStatusEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                statusEnum =  anEnum;
                break;
            }
        }
        return statusEnum;
    }

}
