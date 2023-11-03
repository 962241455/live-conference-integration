/**
 * Demo class
 *
 * @author 1
 * @date 2023/1/5
 */
package com.cmnt.dbpick.common.enums.file;


import java.util.Objects;

/**
 * Demo 1月
 *
 * @author mr . wei
 * @date 2023/1/5
 */
public enum FileTypeEnum {

    /**
     * 文件格式
     */
    FLV("flv", "flv"),
    MP4("mp4", "mp4"),
    MPEG("mpeg", "mpeg"),
    WMV("wmv", "wmv"),
    MKV("mkv", "mkv"),
    AVI("avi", "avi"),
    M3U8("m3u8", "m3u8");

    private final String code;
    private final String name;

    FileTypeEnum(String code, String name) {

        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static FileTypeEnum lookup(String code) {
        for (FileTypeEnum type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No matching BlockTypeEnum constant for [" + code + "]");
    }
    /**
     * Return a string of this channel tag.
     */
    @Override
    public String toString() {
        return this.code;
    }
}
