package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

/**
 * 直播游客观看开关枚举
 * switch_close-关闭； switch_open-开启
 */
public enum LiveVisitorWatchSwitchEnum {

    CLOSE("switch_close", "关闭"),
    OPEN("switch_open", "开启"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    LiveVisitorWatchSwitchEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static LiveVisitorWatchSwitchEnum getByValue(String value) {
        LiveVisitorWatchSwitchEnum ableStatusEnum = UNKNOWN;
        LiveVisitorWatchSwitchEnum[] values = LiveVisitorWatchSwitchEnum.values();
        for (LiveVisitorWatchSwitchEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
