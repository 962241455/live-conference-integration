package com.cmnt.dbpick.common.enums.tencent;

import org.apache.commons.lang3.StringUtils;

/**
 * 视频来源， vod-音视频管理； cos-对象存储
 */
public enum TxVideoSourceType {

    VOD("vod","音视频管理"),
    COS("cos","对象存储"),


    UNKNOWN("unknown","未知类型, 不存在这种情况")
    ;

    private String type;
    private String desc;

    TxVideoSourceType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setCode(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * find enum by channel tag.
     */
    public static TxVideoSourceType lookup(String type) {
        TxVideoSourceType sourceType = UNKNOWN;
        TxVideoSourceType[] values = TxVideoSourceType.values();
        for (TxVideoSourceType anEnum : values) {
            if (StringUtils.equals(anEnum.getType(), type)) {
                sourceType =  anEnum;
                break;
            }
        }
        return sourceType;
    }

}
