package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;


/**
 * 直播观看限制 类型 枚举
 * no_filter-没有限制； register_filter-登记观看 ； visitor_filter-游客观看
 */
public enum LiveWatchFilterTypeEnum {

    NO_FILTER("no_filter", "没有限制"),
    REGISTER_FILTER("register_filter", "登记观看"),
    AUTH_FILTER("auth_filter", "验证码观看"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    LiveWatchFilterTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static LiveWatchFilterTypeEnum getByValue(String value) {
        LiveWatchFilterTypeEnum ableStatusEnum = UNKNOWN;
        LiveWatchFilterTypeEnum[] values = LiveWatchFilterTypeEnum.values();
        for (LiveWatchFilterTypeEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
