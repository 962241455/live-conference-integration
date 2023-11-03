package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;


/**
 * 观看房间类型 枚举
 * live-直播； playback-回放
 */
public enum WatchRoomTypeEnum {

    ALL("all", "直播+回放"),
    LIVE("live", "直播"),
    PLAYBACK("playback", "回放"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    WatchRoomTypeEnum() {
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    WatchRoomTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static WatchRoomTypeEnum getByValue(String value) {
        WatchRoomTypeEnum ableStatusEnum = UNKNOWN;
        WatchRoomTypeEnum[] values = WatchRoomTypeEnum.values();
        for (WatchRoomTypeEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
