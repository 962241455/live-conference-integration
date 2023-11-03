package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 消息类型枚举
 * msg_announcement:公告 ; msg_sign:签到 ; msg_exam:考试 ; msg_question:问卷 ; msg_popup:弹窗
 */
public enum MessageTypeEnum {

    ANNOUNCEMENT("msg_announcement", "公告"),
    SIGN("msg_sign", "签到"),
    EXAM("msg_exam", "考试"),
    QUESTION("msg_question", "问卷"),
    POPUP("msg_popup", "弹窗"),
    COUNTER("msg_counter", "计数器"),
    HOT_ONLINE("msg_hot_online", "热度值在线值"),
    LIVE_START("msg_live_start", "直播开始"),

    LIVE_SCENE("live_scene", "直播现场"),

    UNKNOWN("unknown","未知消息类型, 不存在这种情况");

    private String value;
    private String desc;

    MessageTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static MessageTypeEnum getByValue(String value) {
        MessageTypeEnum ableStatusEnum = UNKNOWN;
        MessageTypeEnum[] values = MessageTypeEnum.values();
        for (MessageTypeEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
