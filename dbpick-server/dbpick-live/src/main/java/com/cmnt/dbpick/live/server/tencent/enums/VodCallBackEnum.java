package com.cmnt.dbpick.live.server.tencent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 视频处理回调枚举
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum VodCallBackEnum {
    PROCEDURE_STATE_CHANGED("ProcedureStateChanged","任务流状态变更"),
    ;


    private String eventType;
    private String handler;


    public static VodCallBackEnum getEnum(String type){
        for (VodCallBackEnum value : VodCallBackEnum.values()) {
            if (StringUtils.equals(type,value.eventType)){
                return value;
            }
        }
        return null;
    }
}
