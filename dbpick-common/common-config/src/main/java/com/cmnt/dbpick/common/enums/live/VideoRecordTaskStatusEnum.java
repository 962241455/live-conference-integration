package com.cmnt.dbpick.common.enums.live;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 云端录制任务的状态信息 枚举
 *     Idle：表示当前录制任务空闲中
 *     InProgress：表示当前录制任务正在进行中。
 *     Exited：表示当前录制任务正在退出的过程中。
 */
public enum VideoRecordTaskStatusEnum {

    Idle("Idle", "当前录制任务空闲中"),
    InProgress("InProgress","前录制任务正在进行中"),
    Exited("Exited","当前录制任务正在退出的过程中"),

    SUCCESS("SUCCESS","录制完成"),
    DEFAULT("DEFAULT","录制失败"),

    UNKNOWN("unknown","未知类型, 不存在这种情况");

    private String value;
    private String desc;

    VideoRecordTaskStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static VideoRecordTaskStatusEnum getByValue(String value) {
        VideoRecordTaskStatusEnum ableStatusEnum = UNKNOWN;
        VideoRecordTaskStatusEnum[] values = VideoRecordTaskStatusEnum.values();
        for (VideoRecordTaskStatusEnum anEnum : values) {
            if (StringUtils.equals(anEnum.getValue(), value)) {
                ableStatusEnum =  anEnum;
                break;
            }
        }
        return ableStatusEnum;
    }
}
