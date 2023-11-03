package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.tx.tencent.response.live.LiveCallBackBaseResponse;
import com.cmnt.dbpick.common.tx.tencent.response.trtc.CallbackBaseResponse;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.live.server.mongodb.document.CallbackEventLog;
import com.cmnt.dbpick.live.server.mongodb.repository.CallbackEventLogRepository;
import com.cmnt.dbpick.live.server.service.CallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 回调事件
 */
@Slf4j
@Service
public class CallbackServiceImpl implements CallbackService {

    public final static String TRTC = "trtc";
    public final static String LIVE = "live";

    @Autowired
    private CallbackEventLogRepository callbackEventLogRepository;

    /**
     * 保存回调日志
     * @return
     */
    @Override
    public Boolean saveTrtcCallbackLog(String ak, String thirdId, String createUser,
                                   CallbackBaseResponse res) {
       log.info("保存Trtc回调日志: response={}",res);
        CallbackEventLog eventLog = CallbackEventLog.builder()
                .ak(ak).thirdId(thirdId)
                .EventSource(TRTC)
                .EventGroupId(res.getEventGroupId()).EventType(res.getEventType())
                .CallbackTs(res.getCallbackTs()).EventInfo(res.getEventInfo())
                .RoomId(res.getEventInfo().getRoomId())
                .EventTs(res.getEventInfo().getEventTs())
                .UserId(res.getEventInfo().getUserId()).build();
        eventLog.initSave(createUser);
        log.info("保存Trtc回调日志: eventLog={}",eventLog);
        callbackEventLogRepository.save(eventLog);
        return Boolean.TRUE;
    }

    @Override
    public Boolean saveLiveCallbackLog(String ak, String thirdId, String createUser, LiveCallBackBaseResponse res) {
        log.info("保存live回调日志: response={}",res);
        CallbackEventLog eventLog = CallbackEventLog.builder()
                .ak(ak).thirdId(thirdId)
                .EventSource(LIVE)
                .EventGroupId(res.getEvent_type()).EventType(res.getCallback_event())
                .CallbackTs(DateUtil.getTimeStrampSeconds()).EventInfo(res)
                .RoomId(res.getStream_id())
                .EventTs(DateUtil.getTimeStrampSeconds())
                .UserId(res.getTask_id()).build();
        eventLog.initSave(createUser);
        log.info("保存live回调日志: eventLog={}",eventLog);
        callbackEventLogRepository.save(eventLog);
        return Boolean.TRUE;
    }


}
