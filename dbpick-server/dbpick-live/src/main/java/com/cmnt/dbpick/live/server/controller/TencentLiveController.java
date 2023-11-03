package com.cmnt.dbpick.live.server.controller;


import com.cmnt.dbpick.live.api.vo.LiveOpenVo;
import com.cmnt.dbpick.live.server.entity.tencent.ShotRuleResponse;
import com.cmnt.dbpick.live.server.entity.tencent.StreamResponse;
import com.cmnt.dbpick.live.server.service.TencentLiveService;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/tencent")
public class TencentLiveController {


    @Autowired
    TencentLiveService tencentLiveService;

    /**
     * 获取推流密钥
     * 1. 用户没认证
     * 2. 直播间已被封禁
     */
    @ApiOperation("获取推流密钥")
    @GetMapping("/liveOpen")
    public ResponsePacket<Object> liveOpen(@PathVariable("room_no") String roomNo){
        // generator push url
        LiveOpenVo liveOpen = tencentLiveService.liveOpen(roomNo);
        return ResponsePacket.onSuccess(liveOpen);
    }

    /**
     * stream push recall
     */
    @ApiOperation("直播开始推流")
    @RequestMapping(value = "/pushLiveStart",method = RequestMethod.POST)
    public ResponsePacket pushLiveStart(@RequestBody StreamResponse response){
        tencentLiveService.pushLiveStart(response);
       return ResponsePacket.onSuccess();
    }

    /**
     * stream end recall
     */
    @ApiOperation("直播推流结束")
    @RequestMapping(value = "/pushLiveEnd",method = RequestMethod.POST)
    public ResponsePacket pushLiveEnd(@RequestBody StreamResponse response){
        tencentLiveService.pushLiveStart(response);
        return ResponsePacket.onSuccess();
    }

    /**
     * screenshot recall
     */
    @RequestMapping(value = "/screenshot",method = RequestMethod.POST)
    public void screenshot(){
        log.info("腾讯云直播截图回调");
    }

    /**
     * appraise salacity recall
     * type 图片类型， 0 ：正常图片， 1 ：色情图片， 2 ：性感图片， 3 ：涉政图片， 4 ：违法图片， 5 ：涉恐图片 ，6 - 9 ：其他其它图片
     */
    @RequestMapping(value = "/imagesAppraise",method = RequestMethod.POST)
    public ResponsePacket appraise(@RequestBody ShotRuleResponse response) {
        log.info("智能鉴黄回调: " + response.toString());
        int confidence = response.getConfidence();
        Integer rid = Integer.valueOf(response.getStreamId());
        log.info("直播截图异常检测: rid = " + rid + ",confidence:" + confidence);
//
//        //LiveDetect Id  多匹配 StreamId ChannelId
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        LiveDetect liveDetect = modelMapper.map(response, LiveDetect.class);
//        liveDetect.setRoomId(rid);
//        StringBuilder builder = new StringBuilder();
//        for (Integer integer : response.getType()) {
//            builder.append(integer).append(",");
//        }
//        String type = builder.toString();
//        liveDetect.setType(type.substring(0,type.length()-1));
//        liveDetect.setHandleStatus(0);
//        if (confidence >= 80){
//            log.info("直播截图异常检测: 置信度>80,系统自动封禁 ");
//            liveDetect.setHandleStatus(1);
//            LocalDateTime resumeTime = LocalDateTime.now().plusDays(3);
//            liveDetect.setResumeTime(resumeTime);
//
//            List<SysPush> sysPushes = sysPushService.list(new QueryWrapper<SysPush>().like("listener_items", "salacity-notice").eq("open",1));
//            sysPushes.forEach(v -> {
//                ArrayList<String> params = new ArrayList<>();
//                params.add(String.valueOf(rid));
//                params.add("直播内容涉黄");
//                if(!StringUtils.isEmpty(v.getEmail())){
//                    mailUtils.sendSimpleMessage(v.getEmail(),"直播内容监控异常","直播内容监控异常 , 房间号:" + rid);
//                }
//                if(!StringUtils.isEmpty(v.getMobile())){
//                    smsUtils.txSmsSend(v.getMobile(),params,"server");
//                }
//                SysPushLog sysPushLog = new SysPushLog();
//                sysPushLog.setSysPushId(v.getId());
//                sysPushLog.setContent("直播内容监控异常，房间号:" + rid);
//                sysPushLogService.save(sysPushLog);
//            });
//
//            // 封号处理
//            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            tencentLiveService.ban(rid, resumeTime.format(df),"自动封禁:涉黄");
//        }
//        liveDetectService.save(liveDetect);
        return  ResponsePacket.onSuccess();
    }

}
