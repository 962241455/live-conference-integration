package com.cmnt.dbpick.common.core.params;

import com.cmnt.dbpick.common.core.enums.FfmpegSymbol;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description ffmpeg 请求参数实体对象封住
 * @date 2022-09-18 12:01
 */
public class FfmpegParam {

    private String key;

    // 某些指令接受多个参数值
    private List<String> values = new ArrayList<String>();

    public FfmpegParam(String key, String... valueArray) {
        this.key = key;
        for (String value : valueArray) {
            values.add(value);
        }
    }

    // 某些指令无参数值
    public FfmpegParam(String key) {
        this(key, new String[0]);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(FfmpegSymbol.separator).append(FfmpegSymbol.param).append(key);
        for (String value : values) {
            sb.append(FfmpegSymbol.separator).append(value);
        }
        return sb.toString();
    }

}
