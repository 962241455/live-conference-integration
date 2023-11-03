package com.cmnt.dbpick.live.server.tencent.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.enums.live.RoomStatusEnum;
import com.cmnt.dbpick.common.tx.tencent.response.trtc.RoomCallbackResponse;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.live.server.service.CallbackService;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import com.cmnt.dbpick.live.server.tencent.enums.TrtcCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.event.TrtcHandlerEventType;
import com.cmnt.dbpick.live.server.tencent.service.TrtcCallBackService;
import com.cmnt.dbpick.user.api.feign.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 实时音视频 直播间回调
 */
@Slf4j
@Component
@TrtcHandlerEventType(TrtcCallBackEnum.TRTC_ROOM)
public class TrtcRoomCallBackServiceImpl implements TrtcCallBackService {

    /**  房间事件组 对应 事件类型 */
    public final static String EVENT_TYPE_CREATE_ROOM = "101";//	创建房间
    public final static String EVENT_TYPE_DISMISS_ROOM	= "102";//	解散房间
    public final static String EVENT_TYPE_ENTER_ROOM = "103";//	进入房间
    public final static String EVENT_TYPE_EXIT_ROOM = "104";//	退出房间
    public final static String EVENT_TYPE_CHANGE_ROLE = "105";//	切换角色

    @Autowired
    private UserClient userClient;

    @Autowired
    private CallbackService callbackServiceImpl;
    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;
//    @Autowired
//    private LiveStatsService liveStatsServiceImpl;
//
//    @Autowired
//    private LiveRoomUserRecordRepository liveRoomUserRecordRepository;

    @Override
    public void execute(String jsonObjectStr) {
        log.info("实时音视频 直播房间 回调返回参数 str={}", jsonObjectStr);
        RoomCallbackResponse res = JSON.parseObject(jsonObjectStr, RoomCallbackResponse.class);
        log.info("实时音视频 直播房间 回调返回参数 response={}", res);
        StreamingRoomVO roomInfo = streamingRoomServiceImpl.detail(res.getEventInfo().getRoomId());
        if(Objects.isNull(roomInfo)){
            log.error("未查到房间信息 ....");
            callbackServiceImpl.saveTrtcCallbackLog("","","",res);
            return;
        }
        String roomNo = roomInfo.getRoomNo();
        switch (res.getEventType()){
            case EVENT_TYPE_CREATE_ROOM://创建房间
                log.info("创建房间 - roomNo={}",roomNo);
                break;
            case EVENT_TYPE_DISMISS_ROOM://解散房间
                log.info("解散房间 - roomNo={}",roomNo);
                //streamingRoomServiceImpl.destroyChatRoom(roomNo);
                streamingRoomServiceImpl.updateRoomStatus(roomNo, RoomStatusEnum.LIVE_OVER.getValue(),"");
                break;
            case EVENT_TYPE_ENTER_ROOM://进入房间
                log.info("记录用户进入房间 - roomNo={}",roomNo);
                //todo 改用es存储
                /*RoomCallbackEvent enterEventInfo = res.getEventInfo();
                saveRecord(TxUserActType.ENTER_ROOM.getValue(), enterEventInfo);*/
            break;
            case EVENT_TYPE_EXIT_ROOM://退出房间
                log.info("记录用户退出房间 - roomNo={}",roomNo);
                //改用es存储
                /*RoomCallbackEvent exitEventInfo = res.getEventInfo();
                saveRecord(TxUserActType.EXIT_ROOM.getValue(), exitEventInfo);*/
                break;
            case EVENT_TYPE_CHANGE_ROLE://切换角色
                break;
            default:
                break;
        }
        callbackServiceImpl.saveTrtcCallbackLog(roomInfo.getAk(),roomInfo.getThirdId(),roomInfo.getCreateUser(),res);

    }

//    /**
//     * 保存用户观看记录
//     */
//    private void saveRecord(String type, RoomCallbackEvent eventInfo){
//        LiveRoomUserRecord record = LiveRoomUserRecord.builder().roomNo(eventInfo.getRoomId())
//                .userId(eventInfo.getUserId()).userRole(UserRoleEnum.lookup(eventInfo.getRole()))
//                .actTime(eventInfo.getEventTs() * 1000).reason(String.valueOf(eventInfo.getReason()))
//                .actType(type).build();
//        if(Objects.nonNull(eventInfo.getTerminalType())){
//            record.setTerminalType(TxTerminalType.lookup(eventInfo.getTerminalType()));
//        }
//        ResponsePacket<UserBaseInfo> result = userClient.findUserBaseInfo(eventInfo.getUserId());
//        if (Objects.nonNull(result) && Objects.nonNull(result.getData())){
//            UserBaseInfo data = result.getData();
//            record.setUserName(data.getUserName());
//            record.setUserAvatar(data.getUserAvatar());
//        }
//        record.initSave("");
//        liveRoomUserRecordRepository.save(record);
//        // 改用es存储
//        //liveStatsServiceImpl.handleRoomUserStats(record);
//    }


}
