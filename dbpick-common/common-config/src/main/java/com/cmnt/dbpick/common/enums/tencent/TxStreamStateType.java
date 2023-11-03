package com.cmnt.dbpick.common.enums.tencent;

/**
 * 流状态：active：活跃， inactive：非活跃， forbid：禁播。
 */
public enum TxStreamStateType {
    ACTIVE("active","活跃"),
    INACTIVE("inactive","非活跃"),
    FORBID("forbid","禁播")
    ;

    private String type;

    private String desc;

    TxStreamStateType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
