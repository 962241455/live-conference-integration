package com.cmnt.dbpick.live.server.tencent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum LiveCallBackEnum {
    LIVE_STOP(0,"直播断流"),
    LIVE_PUSH(1,"直播推流"),
    LIVE_RECORD(100,"直播录制"),
    LIVE_SCREEN(200,"直播截图"),
    LIVE_PORN(317,"直播鉴黄"),
    LIVE_PUSH_TASK(314,"拉流转推")
    ;


    private int eventType;
    private String handler;


    public static LiveCallBackEnum getEnum(Integer type){
        for (LiveCallBackEnum value : LiveCallBackEnum.values()) {
            if (type==value.eventType){
                return value;
            }
        }
        return null;
    }
}
