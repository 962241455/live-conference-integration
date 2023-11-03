package com.cmnt.dbpick.common.core.config;

import com.cmnt.dbpick.common.core.exception.FfmpegConfigurationException;
import io.micrometer.core.instrument.util.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description ffmpeg 配置对象
 * @date 2022-09-18 11:59
 */
public class FfmpegConfig {

    private String ffmpegCommand = "ffmpeg";

    public String getFfmpegCommand() {
        return ffmpegCommand;
    }

    public void setFfmpegCommand(String ffmpegCommand) {
        this.ffmpegCommand = ffmpegCommand;
    }

    public FfmpegConfig() {
        setFfmpegCommand(findExecutable());
    }

    public FfmpegConfig(String ffmpegCommand) {
        super();
        this.ffmpegCommand = ffmpegCommand;
    }

    public String findExecutable() {
        try {
            String osname = System.getProperty("os.name").toLowerCase();
            //String cmd = osname.contains("windows") ? "where.exe ffmpeg" : "which ffmpeg";

            String cmd = osname.contains("windows") ? "where.exe ffmpeg" : "which ffmpeg";

            Process p = Runtime.getRuntime().exec(cmd);

            p.waitFor();

            String text = IOUtils.toString(p.getInputStream(), Charset.defaultCharset()).trim();

            if (text.isEmpty()){
                throw new FfmpegConfigurationException("ffmpeg command was not found in your classpath. " +
                        "Verify its installation or initialize wrapper configurations with correct path/to/ffmpeg");
            }
            setFfmpegCommand(text);

        } catch (IOException e) {
            // log日志记录错误信息, 暂时打印堆栈信息
            e.printStackTrace();
        } catch (InterruptedException e) {
            // log日志记录错误信息, 暂时打印堆栈信息
            e.printStackTrace();
        }
        return getFfmpegCommand();
    }



}
