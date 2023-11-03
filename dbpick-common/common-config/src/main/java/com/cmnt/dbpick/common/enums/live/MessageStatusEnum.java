package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

/**
 * 消息状态枚举
 * no_publish 未发布，publish_ing 发布中，stop_publish 结束发布，again_publish 再次发布
 */
public enum MessageStatusEnum {

    NO_PUBLISH("no_publish", "未发布"),
    PUBLISH_ING("publish_ing", "发布中"),
    STOP_PUBLISH("stop_publish", "结束发布"),
    AGAIN_PUBLISH("again_publish", "再次发布"),

    UNKNOWN("unknown","未知消息类型, 不存在这种情况");

    private String value;
    private String desc;

    MessageStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static MessageStatusEnum getByValue(String value) {
        MessageStatusEnum ableStatusEnum = UNKNOWN;
        MessageStatusEnum[] values = MessageStatusEnum.values();
        for (MessageStatusEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
