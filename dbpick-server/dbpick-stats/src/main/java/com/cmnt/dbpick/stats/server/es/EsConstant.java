package com.cmnt.dbpick.stats.server.es;

/**
 * es 常量配置
 */
public class EsConstant {

    public static final Integer ES_MAX_RESULT_WINDOW = 10000;


    /**
     * es是索引名
     */
    // 房间播放流量索引
    public static final String ES_INDEX_ROOM_FLUX_STATS = "room_flux_stats_index";

    // 视频回放播放流量索引
    public static final String ES_INDEX_FILE_PLAYBACK_STATS = "file_playback_stats_index";


    /**
     * es 观看时长配置参数
     */
    // es 查询观看时长参数
    public static final Integer ES_WATCH_TIMES_PAGE_NO_DEFAULT = 1000;
    public static final Integer ES_WATCH_TIMES_PAGE_SIZE_DEFAULT = 1000;
    //用户单次进出行为统计
    public static final String ES_INDEX_ROOM_USER_RECORD_ONCE = "room_user_record_once_index";
    //用户累计登录时间统计
    public static final String ES_INDEX_ROOM_USER_RECORD_STATS ="room_user_record_stats_index";
    /**
     * 直播间用户行为记录
     */
    public static final String ES_INDEX_ROOM_USER_RECORD ="room_user_record_index";


}
