package com.cmnt.dbpick.common.enums.order;

/**
 * @author zw
 * 订单状态:0待支付 1支付成功 2支付失败 3支付完成（不能退款）4已退款 5待确认 6订单超时
 */
public enum OrderStatusEnum {
    /**
     * 微信
     */
    ORDER_TO_PAY(0,"待支付"),
    ORDER_PAY_SUCCESS(1,"支付成功"),
    ORDER_PAY_FAILED(2,"支付失败"),
    ORDER_PAY_COMPLAIN(3,"支付完成"),
    ORDER_PAY_REFUND(4,"已退款"),
    ORDER_TO_BE_CONFIRMED(5, "待确认"),
    ORDER_PAY_TIME_OUT(6, "已超时")
    ;


    private Integer code;
    private String name;

    OrderStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    public static OrderStatusEnum getEnum(Integer code) {
        OrderStatusEnum[] orderEnum = values();

        for (OrderStatusEnum anEnum : orderEnum) {
            if (anEnum.getCode().equals(code)){
                return anEnum;
            }
        }
        throw new RuntimeException("orderEnum为空,code="+code);
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
