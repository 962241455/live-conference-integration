package com.cmnt.dbpick.common.core.params;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description 批量处理  FfmpegParam 请求参数对象
 * @date 2022-09-18 14:11
 */
public class FfmpegParams {

    private Collection<FfmpegParam> params;

    public FfmpegParams() {
        this.params = new ArrayList<FfmpegParam>();
    }

    public void add(FfmpegParam param, FfmpegParam... params) {
        this.params.add(param);
        this.params.addAll( Arrays.asList( params ) );
    }

    public List<String> getParamsAsStringList() {
        List<String> commandLine = new ArrayList<String>();

        for (FfmpegParam p : params) {
            commandLine.add(p.getKey());

            for (String value : p.getValues()) {
                if (value != null) {
                    commandLine.add(value);
                }
            }
        }

        return commandLine;
    }

}
