package com.cmnt.dbpick.live.server.mq.consumer;


import com.cmnt.dbpick.common.enums.live.MessageTypeEnum;
import com.cmnt.dbpick.common.mq.constant.RocketMQConstant;
import com.cmnt.dbpick.common.tx.tencent.TxCloudImUtil;
import com.cmnt.dbpick.common.tx.tencent.request.im.ImNoticeMessageParam;
import com.cmnt.dbpick.live.api.vo.RoomHotOnlineVO;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoom;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RocketMQMessageListener(
        topic = RocketMQConstant.DELAY_TOPIC_ROOM_HOT_SCORES,
        consumerGroup = RocketMQConstant.DELAY_TOPIC_ROOM_HOT_SCORES)
@Slf4j
public class RoomHotScoresStatsConsumer implements RocketMQListener<String> {

    @Autowired
    private StreamingRoomRepository streamingRoomRepository;

    @Autowired
    private TxCloudImUtil txCloudImUtil;

    @Override
    public void onMessage(String message) {
        log.info("RoomHotScoresStatsConsumer>>>发送im消息同步直播间热度值，message={}",message);
        if(StringUtils.isBlank(message)){
            return;
        }
        StreamingRoom room = streamingRoomRepository.findTop1ByRoomNoOrderByCreateDateDesc(message);
        Integer hotScores = (Objects.nonNull(room) && Objects.nonNull(room.getInfo().getHotScores()))
                ?room.getInfo().getHotScores():0;
        Integer online = (Objects.nonNull(room) && Objects.nonNull(room.getInfo().getOnline()))
                ?room.getInfo().getOnline():0;
        RoomHotOnlineVO vo = RoomHotOnlineVO.builder().roomNo(message)
                .hotScores(hotScores).online(online).build();
        ImNoticeMessageParam msg = ImNoticeMessageParam.builder().msgKey(vo.getRoomNo())
                .groupId(vo.getRoomNo())
                .msgType(MessageTypeEnum.HOT_ONLINE.getValue())
                .msgContent(vo)
                .build();
        log.info("准备发送im热度消息，msg={}",msg);
        try {
            txCloudImUtil.imGroupSystemPush(msg);
        } catch (Exception e) {
            log.error("发送im热度消息失败，msg={}",e.getMessage());
        }

    }

}
