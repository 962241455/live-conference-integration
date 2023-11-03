package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

/**
 * 房间播放记录当前状态
 */
public enum RoomPlayRecordStatusEnum {
    /**
     * 状态：
     * start 开始
     * stop  结束
     */

    START("start", "开始"),
    STOP( "stop","结束"),

    UNKNOWN("unknown", "未知类型")
    ;

    private String status;
    private String msg;

    RoomPlayRecordStatusEnum(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public static RoomPlayRecordStatusEnum getByStatus(String status) {
        RoomPlayRecordStatusEnum statusEnum = UNKNOWN;
        RoomPlayRecordStatusEnum[] values = RoomPlayRecordStatusEnum.values();
        for (RoomPlayRecordStatusEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getStatus(),status)) {
                statusEnum =  anEnum;
                break;
            }
        }
        return statusEnum;
    }


}
