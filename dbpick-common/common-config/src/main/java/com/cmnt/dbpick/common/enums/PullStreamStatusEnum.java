package com.cmnt.dbpick.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 拉流任务状态 枚举
 * enable - 启用，
 * pause - 暂停
 * 未开始,有效,过期
 * @author
 */
public enum PullStreamStatusEnum {

    ENABLE("enable", "启用"),
    PAUSE("pause","暂停"),

    //点播文件开始
    TASK_START("TaskStart","未开始"),
    START("VodSourceFileStart","有效"),
    //点播文件结束
    FINISH("VodSourceFileFinish","过期"),
    TASK_EXIT("TaskExit","过期"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    PullStreamStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static PullStreamStatusEnum getByValue(String value) {
        PullStreamStatusEnum ableStatusEnum = UNKNOWN;
        PullStreamStatusEnum[] values = PullStreamStatusEnum.values();
        for (PullStreamStatusEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
