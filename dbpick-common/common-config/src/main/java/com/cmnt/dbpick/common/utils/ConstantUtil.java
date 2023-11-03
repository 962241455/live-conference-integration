package com.cmnt.dbpick.common.utils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 常量工具
 */
public class ConstantUtil {

    /**
     * 获取首位数字+1 的整数
     * 比如 1 ->2;  128 -> 200 ; 9999 ->10000
     */
    public static Integer ceilNum(Integer num){
        String numLen = String.valueOf(num);
        String numZero = numLen.substring(1);
        StringBuilder ceilNumStr = new StringBuilder();
        ceilNumStr.append(String.valueOf((Integer.valueOf(numLen.substring(0,1))+1)));
        for (int i = 0; i < numZero.length(); i++) {
            ceilNumStr.append("0");
        }
        return Integer.valueOf(ceilNumStr.toString());
    }


    /**
     * 将文件字节大小转为 加上单位的字符串
     * @param fileSize
     * @return
     */
    public static String getFileSizeStrByByte(Long fileSize){
        if(Objects.isNull(fileSize)){
            return "-";
        }
        if(1024*1024 > fileSize && fileSize >= 1024 ) {
            return String.format("%.2f",fileSize.doubleValue()/1024) + "KB";
        }
        if(1024*1024*1024 > fileSize && fileSize >= 1024*1024 ) {
            return String.format("%.2f",fileSize.doubleValue()/(1024*1024)) + "MB";
        }
        if(fileSize >= 1024*1024*1024 ) {
            return String.format("%.2f",fileSize.doubleValue()/(1024*1024*1024)) + "GB";
        }
        return fileSize.toString() + "B";
    }


    /**
     * 查询指定时间范围内的日期
     * (比如 beg=2022-01-01， end=2022-01-10, 返回list （2022-01-01,2022-01-02,2022-01-03....）)
     * @param cntDateBeg
     * @param cntDateEnd
     * @return
     */
    public static List<String> findDaysStr(String cntDateBeg, String cntDateEnd) {
        List<String> list = new ArrayList<>();
        //拆分成数组
        String[] dateBegs = cntDateBeg.split("-");
        String[] dateEnds = cntDateEnd.split("-");
        //开始时间转换成时间戳
        Calendar start = Calendar.getInstance();
        start.set(Integer.valueOf(dateBegs[0]), Integer.valueOf(dateBegs[1]) - 1, Integer.valueOf(dateBegs[2]));
        Long startTIme = start.getTimeInMillis();
        //结束时间转换成时间戳
        Calendar end = Calendar.getInstance();
        end.set(Integer.valueOf(dateEnds[0]), Integer.valueOf(dateEnds[1]) - 1, Integer.valueOf(dateEnds[2]));
        Long endTime = end.getTimeInMillis();
        //定义一个一天的时间戳时长
        Long oneDay = 1000 * 60 * 60 * 24L;
        Long time = startTIme;
        //循环得出
        while (time <= endTime) {
            list.add(new SimpleDateFormat("yyyy-MM-dd" ).format(new Date(time)));
            time += oneDay;
        }
        return list;
    }


}
