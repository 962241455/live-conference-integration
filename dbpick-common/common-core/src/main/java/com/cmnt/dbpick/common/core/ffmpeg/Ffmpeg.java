package com.cmnt.dbpick.common.core.ffmpeg;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.cmnt.dbpick.common.core.config.FfmpegConfig;
import com.cmnt.dbpick.common.core.enums.MediaType;
import com.cmnt.dbpick.common.core.exception.FfmpegTransformException;
import com.cmnt.dbpick.common.core.media.Media;
import com.cmnt.dbpick.common.core.params.FfmpegParam;
import com.cmnt.dbpick.common.core.params.FfmpegParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description ffmpeg 核心功能代码
 * @date 2022-09-18 11:57
 */
public class Ffmpeg {

    private final FfmpegConfig ffmpegConfig;

    private final List<Media> medias;

    private final FfmpegParams params;

    private List<Integer> successValues = new ArrayList<Integer>(Arrays.asList(0));

    /**
     * Timeout to wait while generating a PDF, in seconds
     */
    private int timeout = 10;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Ffmpeg() {
        this(new FfmpegConfig());
    }

    public Ffmpeg(FfmpegConfig ffmpegConfig) {
        super();
        this.ffmpegConfig = ffmpegConfig;
        this.medias = new ArrayList<Media>();
        this.params = new FfmpegParams();
    }

    /**
     * 输入文件
     *
     * @param source
     */
    public void addMediaInput(String source) {
        this.medias.add(new Media(source, MediaType.input));
    }

    /**
     * 输出文件
     *
     * @param source
     */
    public void addMediaOutput(String source) {
        this.medias.add(new Media(source, MediaType.output));
    }

    /**
     * ffmpeg 请求参数
     *
     * @param param
     * @param params
     */
    public void addParam(FfmpegParam param, FfmpegParam... params) {
        this.params.add(param, params);
    }

    public byte[] getFFMPEG() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            // cmd 执行命令
            String command = getCommand();
            System.out.println("输出指令:" + command);
            Process process = Runtime.getRuntime().exec(getCommandAsArray());

            Future<byte[]> inputStreamToByteArray = executor.submit(streamToByteArrayTask(process.getInputStream()));
            Future<byte[]> outputStreamToByteArray = executor.submit(streamToByteArrayTask(process.getErrorStream()));

            process.waitFor();

            if (!successValues.contains(process.exitValue())) {
                byte[] errorStream = getFuture(outputStreamToByteArray);

                throw new FfmpegTransformException(command, process.exitValue(), errorStream,
                        getFuture(inputStreamToByteArray));
            }
            return getFuture(inputStreamToByteArray);
        } finally {
            executor.shutdownNow();
        }

    }

    private Callable<byte[]> streamToByteArrayTask(final InputStream input) {
        return new Callable<byte[]>() {
            public byte[] call() throws Exception {
                return IOUtils.toByteArray(input);
            }
        };
    }

    private byte[] getFuture(Future<byte[]> future) {
        try {
            return future.get(this.timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the final ffmpeg command as string
     *
     * @return the generated command from params
     * @throws IOException
     */
    public String getCommand() throws IOException {
        return StringUtils.join(getCommandAsArray(), " ");
    }

    protected String[] getCommandAsArray() throws IOException {
        List<String> commandLine = new ArrayList<String>();
        // 指令部分ffmpeg
        commandLine.add(ffmpegConfig.getFfmpegCommand());

        commandLine.add("-i");

        // 输入文件
        for (Media media : medias) {
            if (media.getType().equals(MediaType.input)) {
                commandLine.add(media.getSource());
            }
        }

        // 参数部分
        commandLine.addAll(params.getParamsAsStringList());

        // 输出文件
        for (Media media : medias) {
            if (media.getType().equals(MediaType.output)) {
                commandLine.add(media.getSource());
            }
        }
        return commandLine.toArray(new String[commandLine.size()]);
    }

}
