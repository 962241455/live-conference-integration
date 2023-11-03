package com.cmnt.dbpick.stats.server.tencent.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.tx.tencent.response.im.ImCallbackDestroyResponse;
import com.cmnt.dbpick.common.tx.tencent.response.im.eventnfo.StateChangeInfo;
import com.cmnt.dbpick.stats.server.tencent.enums.ImCallbackEnum;
import com.cmnt.dbpick.stats.server.tencent.event.ImHandlerEventType;
import com.cmnt.dbpick.stats.server.tencent.service.ImCallbackService;
import com.cmnt.dbpick.stats.server.utils.StreamingRoomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * im 离开房间
 */
@Slf4j
@Component
@ImHandlerEventType(ImCallbackEnum.DESTROY_GROUP)
public class DestroyGroupCallBackServiceImpl implements ImCallbackService {

    @Autowired
    private StreamingRoomUtil streamingRoomUtil;

    @Override
    public void execute(String jsonObjectStr, StateChangeInfo info) {
        log.info("im 群组解散 回调返回参数 str={}", jsonObjectStr);
        ImCallbackDestroyResponse res = JSON.parseObject(jsonObjectStr, ImCallbackDestroyResponse.class);
        if(Objects.isNull(res) || StringUtils.isBlank(res.getGroupId())){
            log.info("im 群组解散 Response/GroupId is null, res={}",res);
            return;
        }
        //群组解散，停止群内所有投票活动
        streamingRoomUtil.stopVoteByRoomNo(res.getGroupId(), res.getOwner_Account());
    }
}
