package com.cmnt.dbpick.stats.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.tx.tencent.response.TxBaseResponse;
import com.cmnt.dbpick.common.tx.tencent.response.im.eventnfo.StateChangeInfo;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.stats.server.es.document.RoomUserRecordIndex;
import com.cmnt.dbpick.stats.server.es.repository.RoomUserRecordEsRepository;
import com.cmnt.dbpick.stats.server.service.WatchTimesStatsService;
import com.cmnt.dbpick.stats.server.tencent.enums.ImCallbackEnum;
import com.cmnt.dbpick.stats.server.tencent.enums.ImCallbackStateEnum;
import com.cmnt.dbpick.stats.server.tencent.event.ImHandlerEventTypeContext;
import com.cmnt.dbpick.stats.server.tencent.service.ImCallbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * im 回调
 */
@Api(tags = {"im 回调接口"})
@Slf4j
@RestController
@RequestMapping("/im/callback")
public class ImCallbackController {

    @Autowired
    private ImHandlerEventTypeContext eventTypeContext;

    @Autowired
    private WatchTimesStatsService watchTimesStatsServiceImpl;

    @ApiOperation("IM回调")
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    @ResponseBody
    public TxBaseResponse imEventCallback(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        log.info("IM回调参数 jsonObject:{}",jsonObject);
        TxBaseResponse returnJson = new TxBaseResponse();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    jsonObject.put(paramName, paramValue);
                }
            }
        }
        String eventType = jsonObject.getString("CallbackCommand");
        log.info("im回调事件类型： event_type: {}", eventType);
        if(ImCallbackEnum.containType(eventType)){
            /*//mq调 todo
            String mqMsg = JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue);
            mqProducerService.sendTagMsg(mqMsg, MqTagEnum.TAG_ROOM.getTag());*/
            // 直接调
            StateChangeInfo info = null;
            if(StringUtils.equals(eventType,ImCallbackEnum.STATE_CHANGE.getEventType())){
                info = jsonObject.getObject("Info", StateChangeInfo.class);
                log.info("StateChangeInfo:{}",info);
                eventType = ImCallbackStateEnum.getEventTypeByState(info.getAction());
            }
            ImCallbackService service = eventTypeContext.getStrategy(eventType);
            service.execute(JSONObject.toJSONString(jsonObject), info);
        }
        returnJson.setActionStatus("OK");
        returnJson.setErrorInfo("");
        returnJson.setErrorCode(0);
        return returnJson;
    }


/*    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/test")
    public String testMq(@RequestParam("msg") String msg) {
        // 构建消息对象
//        Message<String> msgBody = MessageBuilder.withPayload(msg).build();
//        // 参数一 destination目的地是 rocket的topic
//        rocketMQTemplate.send("live_server_topic_im_room",msgBody);
        SendResult result = rocketMQTemplate.syncSend("live-server-topic-im-room", msg);
        return result.toString();
    }*/


    @Autowired
    private RoomUserRecordEsRepository roomUserRecordEsRepository;

    @GetMapping("/getESData")
    public Object getESData(@RequestParam(value = "esDataId", required = false) String esDataId) {
        //room_user_record, 633d6d2c1c24096b0a931be2_10135_1664970039839
        if(StringUtils.isNotBlank(esDataId)){
            return roomUserRecordEsRepository.findById(esDataId).get();
        }
        Pageable page = PageRequest.of(0,10, Sort.Direction.ASC,"id");
        Page<RoomUserRecordIndex> all = roomUserRecordEsRepository.findAll(page);
        return all.getContent();
    }


    @GetMapping("/delESData")
    public boolean delESData(@RequestParam("esDataId") String esDataId) {
        //room_user_record, 633d6d2c1c24096b0a931be2_10135_1664970039839
        roomUserRecordEsRepository.deleteById(esDataId);
        return true;
    }

    @GetMapping("/saveJoinESData")
    public boolean saveJoinESData(@RequestParam("type") Integer type,
                                  @RequestParam("tm") Integer tm,
                                  @RequestParam(value = "jsonStr", required = false) String jsonStr) {
        String param = "{\"CallbackCommand\":\"State.StateChange\",\"RequestId\":\"2442778428-144115242117253536-Login-Register\",\"EventTime\":1665113769034,\"OptPlatform\":\"Web\",\"Info\":{\"To_Account\":\"633f9ea82b887776fc3dd016_10149\",\"Action\":\"Login\",\"Reason\":\"Register\"},\"ClientIP\":\"218.203.231.240\",\"SdkAppid\":\"1400730667\",\"contenttype\":\"json\"}";
        if(StringUtils.isNotBlank(jsonStr)){
            param = jsonStr;
        }
        if(type==2){
            param = param.replace("Login","Disconnect");
        }

        param = param.replace("1665113769034",String.valueOf(1665113769034L+tm));


        JSONObject jsonObject = JSONObject.parseObject(param);
        String eventType = jsonObject.getString("CallbackCommand");
        StateChangeInfo info = null;
        if(StringUtils.equals(eventType,ImCallbackEnum.STATE_CHANGE.getEventType())){
            info = jsonObject.getObject("Info", StateChangeInfo.class);
            eventType = ImCallbackStateEnum.getEventTypeByState(info.getAction());
            log.info("StateChangeInfo:{}",info);
        }
        ImCallbackService service = eventTypeContext.getStrategy(eventType);
        service.execute(JSONObject.toJSONString(jsonObject), info);
        return true;
    }


    @GetMapping("/redisKey")
    public ResponsePacket redisKey(String redisKey, String pwd) {
        if(StringUtils.isBlank(pwd) || !StringUtils.equals(pwd, DateUtil.now().toString())){
            return ResponsePacket.onSuccess();
        }
        watchTimesStatsServiceImpl.setRedisKey(redisKey);
        return ResponsePacket.onSuccess("set success");
    }

}
