package com.cmnt.dbpick.common.core.exception;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description ffmpeg 转换异常
 * @date 2022-09-18 14:16
 */
@SuppressWarnings("serial")
public class FfmpegTransformException extends RuntimeException {
    private String command;

    private int exitStatus;

    private byte[] out;

    private byte[] err;

    public FfmpegTransformException(String command, int exitStatus, byte[] err, byte[] out) {
        this.command = command;
        this.exitStatus = exitStatus;
        this.err = err;
        this.out = out;
    }

    public String getCommand() {
        return command;
    }

    public int getExitStatus() {
        return exitStatus;
    }

    public byte[] getOut() {
        return out;
    }

    public byte[] getErr() {
        return err;
    }

    @Override
    public String getMessage() {
        return "Process (" + this.command + ") exited with status code " + this.exitStatus + ":\n" + new String(err);
    }

}
