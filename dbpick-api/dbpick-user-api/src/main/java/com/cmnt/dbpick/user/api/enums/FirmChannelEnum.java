package com.cmnt.dbpick.user.api.enums;

/**
 * 直播厂商渠道
 *
 * @author shusong.liang
 **/
public enum FirmChannelEnum {

    /**
     * 腾讯云直播
     */
    TENLIVE_SETTING(1, "C001", "腾讯云直播", "腾讯云"),

    ;
    private final String value;
    private final String name;
    private final int code;
    private final String channelName;

    FirmChannelEnum(int code, String value, String name, String channelName) {
        this.code = code;
        this.value = value;
        this.name = name;
        this.channelName = channelName;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getChannelName() {
        return channelName;
    }

    /**
     * Return a string of this channel tag.
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * find enum by channel tag.
     */
    public static FirmChannelEnum lookup(String channelTag) {
        for (FirmChannelEnum channel : values()) {
            if (channel.value.equals(channelTag)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for channelTag [" + channelTag + "]");
    }

    public static FirmChannelEnum lookup(int code) {
        for (FirmChannelEnum channel : values()) {
            if (channel.code == code) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for code [" + code + "]");
    }

}
