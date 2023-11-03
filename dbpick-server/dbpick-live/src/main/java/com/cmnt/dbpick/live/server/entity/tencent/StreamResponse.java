package com.cmnt.dbpick.live.server.entity.tencent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 * @date
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreamResponse {

    /**
     * https://cloud.tencent.com/document/product/267/32744
     * app : test.domain.com
     * appid : 12345678
     * appname : live
     * channel_id : test_stream
     * errcode : 0
     * errmsg : ok
     * event_time : 1545115790
     * event_type : 1
     * node : 100.121.160.92
     * sequence : 6674468118806626493
     * stream_id :  test_stream
     * stream_param : stream_param=test
     * user_ip : 119.29.94.245
     * sign : ca3e25e5dc17a6f9909a9ae7281e300d
     * t : 1545030873
     */

    private String app;
    private int appid;
    private String appname;
    private String channel_id;
    private int errcode;
    private String errmsg;
    private int event_time;
    private int event_type;
    private String node;
    private String sequence;
    private String stream_id;
    private String stream_param;
    private String user_ip;
    private String sign;
    private int t;

}
