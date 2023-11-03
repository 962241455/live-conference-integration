package com.cmnt.dbpick.stats.server.tencent.service;


import com.cmnt.dbpick.common.tx.tencent.response.im.eventnfo.StateChangeInfo;

/**
 * im回调业务
 */
public interface ImCallbackService {

    void execute(String jsonObjectStr, StateChangeInfo info);

}
