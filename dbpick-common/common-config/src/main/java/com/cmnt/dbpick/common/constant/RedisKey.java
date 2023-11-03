package com.cmnt.dbpick.common.constant;

/**
 * redis key 常量
 *
 * @author
 */
public final class RedisKey {


    /** 初始化账号信息 */
    public static final String BASE_ACCOUNT_INIT = "BASE_ACCOUNT:init";
    /** 自定义账号信息 */
    public static final String BASE_ACCOUNT_SELF = "BASE_ACCOUNT:self";
    /**
     * 直播房间缓存
     * streaming:room
     */
    public static final String STREAMING_ROOM = "streaming:room";

    /**
     * 房间号计数器
     * streaming:room:no
     */
    public static final String STREAMING_ROOM_NO = "streaming:room:no";

    public static final String STREAMING_ROOM_VISITOR_NO = "streaming:visitor:no_%s";

    /**
     * 主播心跳检测
     * streaming:room:heartbeat:streamer:{roomId}:{uid}
     */
    public static final String STREAMING_ROOM_HEARTBEAT_STREAMER_T = "streaming:room:heartbeat:streamer:%s:%s";
    /**
     * 主持人心跳检测
     * streaming:room:heartbeat:presenter:{roomId}:{uid}
     */
    public static final String STREAMING_ROOM_HEARTBEAT_PRESENTER_T = "streaming:room:heartbeat:presenter:%s:%s";

    public static final String REDIS_IM_USER_SIG = "silence:im_user_sig:%s";

    public static final String LIVE_KEY_PREFIX = "LIVING_STATISTIC:%s";
    public static final String LIVE_PRESENT_COUNT = "LIVE_PRESENT_COUNT";
    public static final String LIVE_DAN_MU_COUNT = "LIVE_DAN_MU_COUNT";
    public static final String LIVE_CLICK_COUNT = "LIVE_CLICK_COUNT";


    /**
     * 管理员账号
     */
    public static final String SYS_THIRD_USER_ADMIN = "sys_third_user_admin";
    /**
     * 商户账号相关信息
     */
    public static final String SYS_THIRD_USER = "sys_third_user:%s";
    public static final String SYS_THIRD_USER_ACCESS = "sys_third_user_access:%s";
    //直播间默认配置
    public static final String THIRD_ROOM_CONFIG = "third_room_config:%s";


    //文件上传最大size
    public static final String UPLOAD_FILE_MAX_SIZE_GB = "upload_file_max_size_gb";//视频
    public static final String UPLOAD_FILE_MAX_SIZE_MB = "upload_file_max_size_mb";//图片
    //云点播视频上传签名
    public static final String TENCENT_UPLOAD_SIGN = "tencent_upload_sign";


    /**
     * 用户角色 startv:主播 | watch:观众 | major|管理
     * 角色id
     */
    public static final String LIVE_USER_INFO = "user_info_%s:%s";

    public static final String USER_CURRENT_SYS = "user_info:user_current_sys";

    //用户进出直播间
    public static final String USER_ROOM_ROLE_ACT_TM = "user_room_role_act_tm:%s";


    //断流房间处理时间
    public static final String STOP_STREAM_HANDLE_TIME = "stop_stream_room:handle_time";
    public static final String STOP_STREAM_ROOM_NO = "stop_stream_room:room_no";

    /**
     * 待解散房间号
     */
    public static final String DESTROY_IM_GROUP = "destroy_im_group";
    //回放间聊天室
    public static final String DESTROY_IM_GROUP_PLAYBACK = "destroy_im_group_playback";

    //直播录制任务
    public static final String LIVE_RECORD_TASK = "live_record_task";

    /**
     * 直播间热度值和在线
     */
    //商户所有直播间热度值总和
    public static final String LIVE_USER_HOT_ONLINE = "live_user_hot_online:third_user:user_id_%s";
    //直播间热度值
    public static final String LIVE_ROOM_HOT_ONLINE = "live_room_hot_online:room_no_%s";


    /**
     * 直播间消耗总流量
     * hash(roomNo, sumFlux)
     */
    public static final String LIVE_ROOM_FLUX_SUM = "live_room_flux_sum:third_user:user_id_%s";


    /**
     * 视频重复保存
     */
    public static final String LIVE_VIDEOS_FILE_ID = "live_videos_fileId:fileId_%s";
    /**
     * 直播间回放视频列表
     */
    public static final String LIVE_ROOM_PLAYBACK_VIDEOS = "live_room_playback_videos:room_no_%s:fileId_%s";


    /**
     * 直播间投票信息
     */
    //提交投票用户id
    public static final String ROOM_VOTE_COMMIT_USER = "live_room_vote_info:room_no_%s:voteId_%s:user_%s_commit";

    //总提交人数
    public static final String ROOM_VOTE_COMMIT_TIMES = "live_room_vote_info:room_no_%s:voteId_%s:commitTimes";
    //每个答案提交人数
    public static final String ROOM_VOTE_ANSWER_TIMES = "live_room_vote_info:room_no_%s:voteId_%s:title_%s_answerId_%s";



    //待统计流量数据的历史房间号
    public static final String HIS_ROOM_FULL_FLUX_STATS = "his_room_full_flux_stats";


    private static final String KEY_SEND_SMS_CODE = "_sms_code";

    private static final String OAUTH_KEY = "oauth";

    private static final String APP_DETAILS_KEY = "app_details";

    public static final String UID_TOKEN = "uid_token";

    public static final String SMS_COUNTRY_CODE = "smsCountryCode";

    /**
     * 活动门票剩余数量
     */
    public static final String MEETING_TICKET_NUM = "meeting_ticket_num:meetingId_%s:ticket_id_%s";

    public static final String MEETING_TICKET_USERID = "meeting_ticket_userId:meetingId_%s:ticket_id_%s:user_id_%s";

    /**
     * 短信验证码缓存key
     * @param operation
     * @param phone
     * @param appName
     * @param platform
     * @return
     */
    public static String getKeySendSmsCode(String operation, String phone,String appName,int platform) {
        String key  = "";
        //mobile
        if (platform==1||platform==2){
            key = "mobile_"+appName+KEY_SEND_SMS_CODE;
            //web
        }else {
            key = "web_"+appName+KEY_SEND_SMS_CODE;
        }
        return key + ":" + operation + ":" + phone;
    }

    /**
     * 获取uid的token
     * @param appName
     * @param token
     * @return
     */
    public static String getUidByTokenKey(String appName, String token){

        return appName+":"+token;
    }

    /**
     * 获取oauth用户缓存key
     * @param uid
     * @return
     */
    public static String getOauthUserKey(String uid){
        return OAUTH_KEY+":"+uid;
    }

    public static String getAppDetailsKey(String clientId){
        return APP_DETAILS_KEY+":"+clientId;
    }

    /**
     * 通过uid获取用户uid对应的token
     * @param uid
     * @return
     */
    public static String getUidToken(String uid){
        return UID_TOKEN+":"+uid;
    }


    /**
     * 获取发送国际短信时的国家编码
     * @return
     */
    public static String getSmsCountryCode(){
        return SMS_COUNTRY_CODE;
    }

}
