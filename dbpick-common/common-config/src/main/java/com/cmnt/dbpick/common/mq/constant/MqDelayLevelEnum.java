package com.cmnt.dbpick.common.mq.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * mq 发送消息延迟等级
 * 等级未 0 不延迟
 * 延时消息一共分为18个等级分别为：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
 */
public enum MqDelayLevelEnum {
    NOW(0,"now","立刻发送"),
    SECOND_1(1,"1s","延迟1s发送"),
    SECOND_5(2,"5s","延迟5s发送"),
    SECOND_10(3,"10s","延迟10s发送"),
    SECOND_30(4,"30s","延迟30s发送"),
    MINUTE_1(5,"1m","延迟1m发送"),
    MINUTE_2(6,"2m","延迟2m发送"),
    MINUTE_3(7,"3m","延迟3m发送"),
    MINUTE_4(8,"4m","延迟4m发送"),
    MINUTE_5(9,"5m","延迟5m发送"),
    MINUTE_6(10,"6m","延迟6m发送"),
    MINUTE_7(11,"7m","延迟7m发送"),
    MINUTE_8(12,"8m","延迟8m发送"),
    MINUTE_9(13,"9m","延迟9m发送"),
    MINUTE_10(14,"10m","延迟10m发送"),
    MINUTE_20(15,"20m","延迟20m发送"),
    MINUTE_30(16,"30m","延迟30m发送"),
    HOUR_1(17,"1h","延迟1h发送"),
    HOUR_2(18,"2h","延迟2h发送")
    ;
    private Integer level;
    private String value;
    private String description;

    MqDelayLevelEnum(Integer level, String value, String description) {
        this.level = level;
        this.value = value;
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * find enum by DelayLevel description.
     */
    public static MqDelayLevelEnum lookup(String value) {
        for (MqDelayLevelEnum levelEnum : values()) {
            if (StringUtils.equals(levelEnum.value, value)) {
                return levelEnum;
            }
        }
        return NOW;
    }

}
