package com.cmnt.dbpick.stats.server.tencent.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.enums.tencent.TxUserActType;
import com.cmnt.dbpick.common.tx.tencent.response.im.ImCallbackJoinResponse;
import com.cmnt.dbpick.common.tx.tencent.response.im.eventnfo.NewMember;
import com.cmnt.dbpick.common.tx.tencent.response.im.eventnfo.StateChangeInfo;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.stats.api.params.RoomUserRecordParam;
import com.cmnt.dbpick.stats.server.es.document.RoomUserRecordIndex;
import com.cmnt.dbpick.stats.server.tencent.enums.ImCallbackEnum;
import com.cmnt.dbpick.stats.server.tencent.event.ImHandlerEventType;
import com.cmnt.dbpick.stats.server.tencent.service.ImCallbackService;
import com.cmnt.dbpick.stats.server.service.RoomUserRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * im 进入房间
 */
@Slf4j
@Component
@ImHandlerEventType(ImCallbackEnum.JOIN_GROUP)
public class JoinGroupCallBackServiceImpl implements ImCallbackService {

    @Autowired
    private RoomUserRecordService roomUserRecordServiceImpl;

    @Override
    public void execute(String jsonObjectStr, StateChangeInfo info) {
        log.info("im 进入房间 回调返回参数 str={}", jsonObjectStr);
        ImCallbackJoinResponse res = JSON.parseObject(jsonObjectStr, ImCallbackJoinResponse.class);
        RoomUserRecordParam param = new RoomUserRecordParam();
        FastBeanUtils.copy(res, param);
        String actReason = res.getJoinType();
        List<String> memberIdList = new ArrayList<>();
        List<NewMember> memberList = res.getNewMemberList();
        if(Objects.nonNull(info)){
            actReason = StringUtils.isBlank(actReason) ? info.getReason():  actReason;
            memberIdList = new ArrayList(){{add(info.getTo_Account());}};
        }
        if( Objects.nonNull(memberList) && !memberList.isEmpty()){
            memberIdList = memberList.stream().map(NewMember::getMember_Account).collect(Collectors.toList());
        }
        if(Objects.isNull(memberIdList) || memberIdList.isEmpty()){
            log.info("im 进入房间 新用户为空 userList={},info={},str={}", memberIdList,info,jsonObjectStr);
            return;
        }
        param.setActType(TxUserActType.ENTER_ROOM.getValue());
        param.setActReason(actReason);
        param.setMemberIdList(memberIdList);
        List<RoomUserRecordIndex> recordList = roomUserRecordServiceImpl.saveRoomUserRecord(param);
        roomUserRecordServiceImpl.handleRoomHotAndOnline(recordList);
        //roomUserRecordServiceImpl.saveRecordListByRoomNo(recordList);
    }

}
