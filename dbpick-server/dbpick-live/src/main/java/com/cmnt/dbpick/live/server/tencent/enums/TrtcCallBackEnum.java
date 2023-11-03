package com.cmnt.dbpick.live.server.tencent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum TrtcCallBackEnum {
    TRTC_ROOM(1,"房间事件"),
    TRTC_MEDIA(2,"媒体事件"),
    TRTC_RECORDING(3,"云端录制事件")
    ;

    private int eventType;
    private String handler;


    public static TrtcCallBackEnum getEnum(Integer type){
        for (TrtcCallBackEnum value : TrtcCallBackEnum.values()) {
            if (type==value.eventType){
                return value;
            }
        }
        return null;
    }
}
