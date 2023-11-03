package com.cmnt.dbpick.common.constant;


public class Constants {

    public static final String CMNT_DEFAULT_LOG = "https://framework-1320407621.cos.ap-beijing.myqcloud.com/icon/logolive.png";
    public static final String CMNT_DEFAULT_PLATFORM = "cmnt";
    public static final String CMNT_DEFAULT_APPNAME = "live";
    public static final String CMNT_PRESENTER_PREFIX = "主持人";
    public static final String CMNT_STREAMER_PREFIX = "老师";
    public static final String CMNT_WATCH_PREFIX = "观众";
    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 用于在解密的时候
     */
    public static String THIRDPARTY_FLAG = "thridparty";
    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 盐值取值范围
     * @return
     */
    public static final String SALT_VALUE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final Integer SALT_LENGTH = 10;

    public static String getDefaultImgae(){
        String url = "https://cmnt-live.obs.cn-east-3.myhuaweicloud.com/default_avatar/%s.jpg";
        int n = (int)(Math.random()*(30-1+1))+1;
        url = String.format(url, n);
        System.out.println(url);
        return url;
    }

    public static void main(String[] args) {
        for (int i = 0; i<= 150; i++){
            getDefaultImgae();
        }
    }

}
