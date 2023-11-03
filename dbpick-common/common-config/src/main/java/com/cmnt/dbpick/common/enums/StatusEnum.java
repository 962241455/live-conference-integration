package com.cmnt.dbpick.common.enums;

/**
 * @author
 * @description TODO
 * @date 2022-05-25
 */
public enum StatusEnum {
    FAILED(0,"失败"),
    SUCCESS(1,"成功"),

    ENABLE(1, "启用"),
    DISABLE (0, "禁用");
    ;
    private Integer code;
    private String name;

    StatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
