package com.cmnt.dbpick.stats.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.utils.RedisUtils;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.stats.api.vo.*;
import com.cmnt.dbpick.stats.server.service.SettlementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@Slf4j
@Api(tags = {"直播结算数据相关接口"})
@RequestMapping("/settlement")
@RestController
public class SettlementController extends BaseController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SettlementService settlementServiceImpl;


    @ApiOperation("查询商户指定时间内的播放详情(播放流量，转码时长，回放流量)")
    @GetMapping("/detail")
    public ResponsePacket<SettlementDataVO> settlementDetail(ThirdRoomQueryParam param) {
        log.info("查询商户指定时间内的播放详情(播放流量，转码时长，回放流量) param={}", param);
        getParamAccess(param);
        if(StringUtils.isBlank(param.getThirdId())){
            throw new BizException("请输入用户账号信息");
        }
        String redisKey = String.format(RedisKey.SETTLEMENT_THIRD_DATE, param.getThirdId(),
                param.getSearchStartTime()+"_"+param.getSearchEndTime());
        Object redisVo = redisUtils.get(redisKey);
        SettlementDataVO vo = new SettlementDataVO();
        if(Objects.isNull(redisVo)){
            vo = SettlementDataVO.builder()
                    .playFluxVO(settlementServiceImpl.thirdPlayFlux(param))
                    .transVideoVO(settlementServiceImpl.thirdTransVideo(param))
                    .playbackVO(settlementServiceImpl.thirdPlaybackFlux(param))
                    .build();
            redisUtils.set(redisKey, JSON.toJSONString(vo),60*60L);//有效期1小时
        }else{
            log.info("查询商户指定时间内的播放详情(播放流量，转码时长，回放流量) redisVo={}", redisVo);
            vo = JSONObject.toJavaObject(JSONObject.parseObject(redisVo.toString()), SettlementDataVO.class);
        }
        return ResponsePacket.onSuccess(vo);
    }


    @ApiOperation("查询商户指定时间内的播放流量")
    @GetMapping("/flux")
    public ResponsePacket<SettlementFluxVO> thirdPlayFlux(ThirdRoomQueryParam param) {
        log.info("查询商户指定时间内的播放流量 param={}", param);
        getParamAccess(param);
        if(StringUtils.isBlank(param.getThirdId())){
            throw new BizException("请输入用户账号信息");
        }
        return ResponsePacket.onSuccess(settlementServiceImpl.thirdPlayFlux(param));
    }


    @ApiOperation("查询商户指定时间内的视频转码信息")
    @GetMapping("/trans")
    public ResponsePacket<SettlementTransVO> thirdTransVideo(ThirdRoomQueryParam param) {
        log.info("查询商户指定时间内的视频转码信息 param={}", param);
        getParamAccess(param);
        if(StringUtils.isBlank(param.getThirdId())){
            throw new BizException("请输入用户账号信息");
        }
        return ResponsePacket.onSuccess(settlementServiceImpl.thirdTransVideo(param));
    }



    @ApiOperation("查询商户指定时间内的回放流量")
    @GetMapping("/playback")
    public ResponsePacket<SettlementPlaybackVO> thirdPlaybackFlux(ThirdRoomQueryParam param) {
        log.info("查询商户指定时间内的回放流量 param={}", param);
        getParamAccess(param);
        if(StringUtils.isBlank(param.getThirdId())){
            throw new BizException("请输入用户账号信息");
        }
        return ResponsePacket.onSuccess(settlementServiceImpl.thirdPlaybackFlux(param));
    }





}
