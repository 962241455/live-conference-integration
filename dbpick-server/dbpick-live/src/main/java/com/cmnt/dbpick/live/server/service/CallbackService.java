package com.cmnt.dbpick.live.server.service;


import com.cmnt.dbpick.common.tx.tencent.response.live.LiveCallBackBaseResponse;
import com.cmnt.dbpick.common.tx.tencent.response.trtc.CallbackBaseResponse;

/**
 * trtc 回调事件
 */
public interface CallbackService {

    /**
     * 保存回调日志
     * @return
     */
    Boolean saveTrtcCallbackLog(String ak, String thirdId, String createUser,
                            CallbackBaseResponse res);

    /**
     * 保存回调日志
     * @return
     */
    Boolean saveLiveCallbackLog(String ak, String thirdId, String createUser,
                                LiveCallBackBaseResponse res);
}
