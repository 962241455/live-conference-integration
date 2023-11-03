package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.live.api.vo.TxConfigVo;

public interface TRTCService {

    TxConfigVo getTencentConfig();

    void dissolveRoom(String uid, Long roomId);

    void kickOutUser(Long roomId, String... uids);


}
