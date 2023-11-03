package com.cmnt.dbpick.common.enums;

/**
 * 角色类型
 *
 * @author shusong.liang
 **/
public enum UserRoleEnum {

    /**
     * 角色类型
     */
    STARTV(20, "startv", "主播"),
    WATCH(21, "watch", "观众"),
    MAJOR(1, "major", "管理"),

    ;
    private final String value;
    private final String name;
    private final int code;

    UserRoleEnum(int code, String value, String name) {
        this.code = code;
        this.value = value;
        this.name = name;
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
    public static UserRoleEnum lookup(String channelTag) {
        for (UserRoleEnum channel : values()) {
            if (channel.value.equals(channelTag)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for channelTag [" + channelTag + "]");
    }

    public static String lookup(int code) {
        for (UserRoleEnum channel : values()) {
            if (channel.code == code) {
                return channel.getValue();
            }
        }
        return String.valueOf(code);
        //throw new IllegalArgumentException("No matching channel constant for code [" + code + "]");
    }

}
