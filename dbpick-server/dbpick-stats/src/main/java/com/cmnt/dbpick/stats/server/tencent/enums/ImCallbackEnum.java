package com.cmnt.dbpick.stats.server.tencent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * IM回调枚举
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ImCallbackEnum {
    JOIN_GROUP("Group.CallbackAfterNewMemberJoin","进入房间"),
    EXIT_GROUP("Group.CallbackAfterMemberExit","离开房间"),
    STATE_CHANGE("State.StateChange","状态变更"),

    DESTROY_GROUP("Group.CallbackAfterGroupDestroyed","解散群组"),
    ;

    private String eventType;
    private String handler;


    public static ImCallbackEnum getEnum(String type){
        for (ImCallbackEnum value : ImCallbackEnum.values()) {
            if (StringUtils.equals(type,value.eventType)){
                return value;
            }
        }
        return null;
    }


    public static Boolean containType(String type){
        for (ImCallbackEnum value : ImCallbackEnum.values()) {
            if (StringUtils.equals(type,value.eventType)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

}
