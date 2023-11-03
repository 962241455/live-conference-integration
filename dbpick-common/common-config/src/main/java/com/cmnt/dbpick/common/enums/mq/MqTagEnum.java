package com.cmnt.dbpick.common.enums.mq;

/**
 * mq tag 标签
 * @author
 */
public enum MqTagEnum {
    CYSD_SERVER_TOPIC("CYSD_SERVER_TOPIC","消费主题"),
    TAG_MARKET_ORDER_DELAY_CLOSE("TAG_MARKET_ORDER_DELAY_CLOSE","订单延迟队列到期关闭"),
    TAG_CICC_SPLIT("TAG_CICC_SPLIT","cicc 分账tag"),
    DELAY_TOPIC_VIDEO_TRANSCODING_START("DELAY_TOPIC_VIDEO_TRANSCODING_START","开始转码"),

    ;
    private String tag;
    private String description;

    MqTagEnum(String tag, String description) {
        this.tag = tag;
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
