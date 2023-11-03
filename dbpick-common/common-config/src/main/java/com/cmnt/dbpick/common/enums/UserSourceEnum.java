package com.cmnt.dbpick.common.enums;

/**
 * 用户来源
 **/
public enum UserSourceEnum {

    /**
     * 角色类型
     */
    BACKEND("backend", "后台"),
    THIRD("third", "第三方"),
    VISITOR("visitor", "游客"),
    REGISTER("register", "用户"),

    ;
    private final String value;
    private final String name;

    UserSourceEnum(String value, String name) {
        this.value = value;
        this.name = name;
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
    public static UserSourceEnum lookup(String channelTag) {
        for (UserSourceEnum channel : values()) {
            if (channel.value.equals(channelTag)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for channelTag [" + channelTag + "]");
    }


}
