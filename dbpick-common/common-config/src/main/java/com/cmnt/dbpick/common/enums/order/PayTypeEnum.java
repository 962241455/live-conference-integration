package com.cmnt.dbpick.common.enums.order;


import java.util.Objects;

/**
 * 支付类型:1-微信;2-支付宝;3-云闪付;4-银行卡 5-现金
 *
 * @author mr . wei
 * @date 2023/04/1
 */
public enum PayTypeEnum {

    /**
     * 支付类型:1-微信;2-支付宝;3-云闪付;4-银行卡 5-现金
     */
    WECHAT(1,"微信"),
    AILIPAY(2,"支付宝"),
    NEST_BEAN(3,"云闪付"),
    BANK_CARD(4,"银行卡"),
    READY_MONEY(5,"现金"),
    ;
    private Integer code;
    private String msg;

    PayTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static PayTypeEnum lookup(Integer code) {
        for (PayTypeEnum type : values()) {
            if (Objects.equals(type.code,code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No matching BlockTypeEnum constant for [" + code + "]");
    }
}
