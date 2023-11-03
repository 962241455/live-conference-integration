package com.cmnt.dbpick.common.enums.order;

/**
 * @author zw
 * 订单类型:1-会员 2-会议 3-缴费 4-门票 5-其他
 */
public enum OrderTypeEnum {
    /**
     * 订单类型:1-会员 2-会议 3-缴费 4-门票 5-其他
     */
    ORDER_TO_MEMBER(1,"会员"),
    ORDER_TO_MEETING(2,"会议"),
    ORDER_TO_JPAY(3,"缴费"),
    ORDER_TO_ENTRANCE_TICKET(4,"门票"),
    ORDER_TO_OTHER(5,"其他");

    private Integer code;
    private String name;

    OrderTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OrderTypeEnum lookup(Integer code) {
        for (OrderTypeEnum channel : values()) {
            if (channel.code.equals(code)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for channelTag [" + code + "]");
    }

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
