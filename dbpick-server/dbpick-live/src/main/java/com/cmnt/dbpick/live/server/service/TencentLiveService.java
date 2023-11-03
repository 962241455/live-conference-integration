/**
 * Demo class
 *
 * @author 28021
 * @date 2022/7/25
 */
package com.cmnt.dbpick.live.server.service;


import com.cmnt.dbpick.live.api.vo.LiveOpenVo;
import com.cmnt.dbpick.live.server.entity.tencent.StreamResponse;

/**
 * 腾讯live 模块
 *
 * @author mr . wei
 * @date 2022/7/25
 */
public interface TencentLiveService {

    boolean ban(Integer rid, String resumeTime, String reason);

    LiveOpenVo liveOpen(String roomNo);
    LiveOpenVo getSecretByRoomNo(String roomNo);

    boolean pushLiveStart(StreamResponse response);

    boolean pushLiveEnd(StreamResponse response);

    boolean resume(Integer rid);

}
