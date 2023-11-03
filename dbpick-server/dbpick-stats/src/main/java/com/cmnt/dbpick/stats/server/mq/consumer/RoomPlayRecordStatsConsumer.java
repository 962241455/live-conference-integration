package com.cmnt.dbpick.stats.server.mq.consumer;


import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.mq.constant.RocketMQConstant;
import com.cmnt.dbpick.live.api.message.RoomPlayRecordMessage;
import com.cmnt.dbpick.stats.server.mq.event.SameDayHandlerEventContext;
import com.cmnt.dbpick.stats.server.mq.service.RoomPlayRecordStatsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = RocketMQConstant.DELAY_TOPIC_ROOM_PLAY_RECORD,
        consumerGroup = RocketMQConstant.DELAY_TOPIC_ROOM_PLAY_RECORD)
@Slf4j
public class RoomPlayRecordStatsConsumer implements RocketMQListener<String> {

    @Autowired
    private SameDayHandlerEventContext sameDayHandlerEventContext;

    @Override
    public void onMessage(String message) {
        log.info("RoomPlayRecordStatsConsumer>>>根据直播记录统计直播数据，message={}",message);
        if(StringUtils.isBlank(message)){
            return;
        }
        RoomPlayRecordMessage record = JSONObject.toJavaObject(JSONObject.parseObject(message), RoomPlayRecordMessage.class);
        RoomPlayRecordStatsService strategy = sameDayHandlerEventContext.getStrategy(record.getStartTime(), record.getStopTime());
        strategy.execute(record);
    }

}
