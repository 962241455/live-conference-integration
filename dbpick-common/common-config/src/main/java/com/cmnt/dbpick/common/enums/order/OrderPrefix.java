package com.cmnt.dbpick.common.enums.order;

public enum OrderPrefix {
    /**
     * 订单类型区别 会员YH 会议HY 支付PY 课程KC
     */
    YH("YH"),
    HY("HY"),
    PY("PY"),
    TR("TR"),
    KC("KC"),
    ;

    private final String type;

    OrderPrefix(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
