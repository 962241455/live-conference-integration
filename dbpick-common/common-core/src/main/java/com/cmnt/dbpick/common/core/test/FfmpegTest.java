package com.cmnt.dbpick.common.core.test;

import com.cmnt.dbpick.common.core.ffmpeg.Ffmpeg;
import com.cmnt.dbpick.common.core.params.FfmpegParam;

import java.io.IOException;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description 描述
 * @date 2022-09-18 14:21
 */
public class FfmpegTest {

    // 视频转换：mp4 转换为flv
    // ffmpeg 指令:C:\ffmpg\ffmpeg\bin\ffmpeg.exe -i C:\ffmpg\ffmpeg\bin\test.mp4 -acodec copy -vcodec copy -f flv C:\ffmpg\ffmpeg\bin\output.flv
    public static void mp4TransformFlv() throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        Ffmpeg ffmpeg = new Ffmpeg();
        ffmpeg.addMediaInput("D:\\ffmpg\\ffmpeg\\input\\test2.mp4");
        ffmpeg.addMediaOutput("D:\\ffmpg\\ffmpeg\\output\\test2.flv");
        ffmpeg.addParam(new FfmpegParam("-acodec", new String[]{"copy"}), new FfmpegParam[]{new FfmpegParam("-vcodec", new String[]{"copy"}),new FfmpegParam("-f", new String[]{"flv"})});
        byte[] bytes = ffmpeg.getFFMPEG();
        System.out.println(new String(bytes));
    }

    // 视频转换：mp4 转换为h264
    // ffmpeg 指令:C:\ffmpg\ffmpeg\bin\ffmpeg.exe -i C:\ffmpg\ffmpeg\bin\test.mp4 -codec copy -bsf h264_mp4toannexb -f h264 C:\ffmpg\ffmpeg\bin\output.h264
    public static void mp4TransformH264() throws IOException, InterruptedException {
        Ffmpeg ffmpeg = new Ffmpeg();
        ffmpeg.addMediaInput("C:\\ffmpg\\ffmpeg\\bin\\test.mp4");
        ffmpeg.addMediaOutput("C:\\ffmpg\\ffmpeg\\bin\\output.h264");
        ffmpeg.addParam(new FfmpegParam("-codec", new String[]{"copy"}), new FfmpegParam[]{new FfmpegParam("-bsf", new String[]{"h264_mp4toannexb"}),new FfmpegParam("-f", new String[]{"h264"})});
        byte[] bytes = ffmpeg.getFFMPEG();
        System.out.println(new String(bytes));
    }

    // 视频转换:mp4 转换为ts
    // ffmpeg 指令:C:\ffmpg\ffmpeg\bin\ffmpeg.exe -i C:\ffmpg\ffmpeg\bin\test.mp4 -codec copy -bsf h264_mp4toannexb C:\ffmpg\ffmpeg\bin\test.ts
    public static void mp4TransformTs() throws IOException, InterruptedException{
        Ffmpeg ffmpeg = new Ffmpeg();
        ffmpeg.addMediaInput("C:\\ffmpg\\ffmpeg\\bin\\test.mp4");
        ffmpeg.addMediaOutput("C:\\ffmpg\\ffmpeg\\bin\\test.ts");
        ffmpeg.addParam(new FfmpegParam("-codec", new String[]{"copy"}), new FfmpegParam[]{new FfmpegParam("-bsf", new String[]{"h264_mp4toannexb"})});
        byte[] bytes = ffmpeg.getFFMPEG();
        System.out.println(new String(bytes));
    }

    // 视频转换: ts 转换为mp4
    // ffmpeg 指令:C:\ffmpg\ffmpeg\bin\ffmpeg.exe -i C:\ffmpg\ffmpeg\bin\test.ts -acodec copy -vcodec copy -f mp4 C:\ffmpg\ffmpeg\bin\output.mp4
    public static void tsTransformMp4() throws IOException, InterruptedException{
        Ffmpeg ffmpeg = new Ffmpeg();
        ffmpeg.addMediaInput("C:\\ffmpg\\ffmpeg\\bin\\test.ts");
        ffmpeg.addMediaOutput("C:\\ffmpg\\ffmpeg\\bin\\output.mp4");
        ffmpeg.addParam(new FfmpegParam("-acodec", new String[]{"copy"}), new FfmpegParam[]{new FfmpegParam("-vcodec", new String[]{"copy"}),new FfmpegParam("-f", new String[]{"mp4"})});
        byte[] bytes = ffmpeg.getFFMPEG();
        System.out.println(new String(bytes));
    }

    // 视频转换：ts 转换为flv
    // ffmpeg 指令：C:\ffmpg\ffmpeg\bin\ffmpeg.exe -i C:\ffmpg\ffmpeg\bin\test.ts -acodec copy -vcodec copy -f flv C:\ffmpg\ffmpeg\bin\123.flv
    public static void tsTransformFlv() throws IOException, InterruptedException{
        Ffmpeg ffmpeg = new Ffmpeg();
        ffmpeg.addMediaInput("C:\\ffmpg\\ffmpeg\\bin\\test.ts");
        ffmpeg.addMediaOutput("C:\\ffmpg\\ffmpeg\\bin\\123.flv");
        ffmpeg.addParam(new FfmpegParam("-acodec", new String[]{"copy"}), new FfmpegParam[]{new FfmpegParam("-vcodec", new String[]{"copy"}),new FfmpegParam("-f", new String[]{"flv"})});
        byte[] bytes = ffmpeg.getFFMPEG();
        System.out.println(new String(bytes));
    }

    // 视频分离之视频分离
    // ffmpeg 指令:C:\ffmpg\ffmpeg\bin\ffmpeg.exe -i C:\ffmpg\ffmpeg\bin\music.mp4 -vn  -f mp3 -y  C:\ffmpg\ffmpeg\bin\test.mp3
    public static void videoDelete() throws IOException, InterruptedException{
        Ffmpeg ffmpeg = new Ffmpeg();
        ffmpeg.addMediaInput("C:\\ffmpg\\ffmpeg\\bin\\demo.mp4");
        ffmpeg.addMediaOutput("C:\\ffmpg\\ffmpeg\\bin\\test.mp3");
        ffmpeg.addParam(new FfmpegParam("-vn", new String[]{""}), new FfmpegParam[]{new FfmpegParam("-f", new String[]{"mp3"}),new FfmpegParam("-y", new String[]{""})});
        byte[] bytes = ffmpeg.getFFMPEG();
        System.out.println(new String(bytes));
    }
    // 视频分离之音频分离
    // ffmpeg指令:C:\ffmpg\ffmpeg\bin\ffmpeg.exe -i C:\ffmpg\ffmpeg\bin\demo.mp4 -an  -vcodec copy C:\ffmpg\ffmpeg\bin\demo1.mp4
    public static void audioDelete() throws IOException, InterruptedException{
        Ffmpeg ffmpeg = new Ffmpeg();
        ffmpeg.addMediaInput("C:\\ffmpg\\ffmpeg\\bin\\demo.mp4");
        ffmpeg.addMediaOutput("C:\\ffmpg\\ffmpeg\\bin\\demo1.mp4");
        ffmpeg.addParam(new FfmpegParam("-an", new String[]{""}), new FfmpegParam[]{new FfmpegParam("-vcodec", new String[]{"copy"})});
        byte[] bytes = ffmpeg.getFFMPEG();
        System.out.println(new String(bytes));
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        // 视频文件之转换
        mp4TransformFlv();
        //mp4TransformH264();
        //mp4TransformTs();
        //tsTransformMp4();
        //tsTransformFlv();

        // 视频文件之音频/视频分离
        //videoDelete();
        //audioDelete();
    }

}
