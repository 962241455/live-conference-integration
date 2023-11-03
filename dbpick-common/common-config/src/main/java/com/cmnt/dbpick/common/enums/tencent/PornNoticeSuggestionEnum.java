package com.cmnt.dbpick.common.enums.tencent;

/**
 * 鉴黄通知 建议值
 */
public enum PornNoticeSuggestionEnum {


    BLOCK("Block","打击"),
    REVIEW("Review","待复审"),
    PASS("Block","正常"),
    ;

    private String type;

    private String desc;

    PornNoticeSuggestionEnum(String type, String desc) {
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
