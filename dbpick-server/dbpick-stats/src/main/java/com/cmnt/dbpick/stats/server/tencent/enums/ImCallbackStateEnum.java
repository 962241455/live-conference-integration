package com.cmnt.dbpick.stats.server.tencent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * IM（状态变更回调）枚举
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ImCallbackStateEnum {

    Login("Login", "Group.CallbackAfterNewMemberJoin","上线（TCP 建立）"),
    Logout("Logout", "Group.CallbackAfterMemberExit","下线（TCP 断开）"),
    Disconnect("Disconnect", "Group.CallbackAfterMemberExit","网络断开（TCP 断开）"),
    ;

    private String stateType;
    private String eventType;
    private String handler;


    public static ImCallbackStateEnum getEnum(String type){
        for (ImCallbackStateEnum value : ImCallbackStateEnum.values()) {
            if (StringUtils.equals(type,value.eventType)){
                return value;
            }
        }
        return null;
    }


    public static String getEventTypeByState(String state){
        for (ImCallbackStateEnum value : ImCallbackStateEnum.values()) {
            if (StringUtils.equals(state,value.stateType)){
                return value.eventType;
            }
        }
        return null;
    }


    public static Boolean containType(String type){
        for (ImCallbackStateEnum value : ImCallbackStateEnum.values()) {
            if (StringUtils.equals(type,value.eventType)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

}
