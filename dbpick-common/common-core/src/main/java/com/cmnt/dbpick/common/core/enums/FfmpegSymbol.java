package com.cmnt.dbpick.common.core.enums;



/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description ffmpeg 符号
 * @date 2022-09-18 11:59
 */
public enum FfmpegSymbol {

    separator(" "), param("");

    private final String symbol;

    FfmpegSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

}
