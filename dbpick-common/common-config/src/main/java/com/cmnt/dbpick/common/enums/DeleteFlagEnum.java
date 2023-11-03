package com.cmnt.dbpick.common.enums;

/**
 * 删除标记
 * @author
 */
public enum DeleteFlagEnum {
    /**
     *
     * 删除标记 0-未删除 1-已删除
     *
     */
    NOT_DELETED(0),
    DELETED(1);

    private final int type;

    DeleteFlagEnum(final int type) {
        this.type = type;
    }

    public int getValue() {
        return type;
    }
}
