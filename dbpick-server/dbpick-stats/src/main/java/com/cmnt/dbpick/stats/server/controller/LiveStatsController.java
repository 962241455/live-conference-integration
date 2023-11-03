package com.cmnt.dbpick.stats.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.stats.api.params.QueryRoomStatsParams;
import com.cmnt.dbpick.stats.api.vo.*;
import com.cmnt.dbpick.stats.server.service.RoomFluxStatsService;
import com.cmnt.dbpick.stats.server.service.RoomInfoRecordService;
import com.cmnt.dbpick.stats.server.service.RoomUserRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@Api(tags = {"直播统计数据相关接口"})
@RequestMapping("/live")
@RestController
public class LiveStatsController extends BaseController {

    @Autowired
    private RoomUserRecordService roomUserRecordServiceImpl;
    @Autowired
    private RoomInfoRecordService roomInfoRecordServiceImpl;
    @Autowired
    private RoomFluxStatsService roomFluxStatsServiceImpl;

    @ApiOperation("查询直播间播放流量")
    @GetMapping("/room/flux")
    public ResponsePacket<RoomFluxStatsVO> roomPlayFlux(RoomNoParam param) {
        log.info("查询直播间播放流量 param={}", param);
        return ResponsePacket.onSuccess(roomFluxStatsServiceImpl.roomPlayFlux(param));
    }


    @ApiOperation("查询商户指定播放时间内的每个直播间的播放流量")
    @GetMapping("/user/flux")
    public ResponsePacket<List<UserRoomFluxVO>> userRoomPlayFlux(ThirdRoomQueryParam param) {
        log.info("查询商户指定播放时间内的每个直播间的播放流量 param={}", param);
        getParamAccess(param);
        if(StringUtils.isBlank(param.getThirdId())){
            throw new BizException("请输入用户账号信息");
        }
        return ResponsePacket.onSuccess(roomFluxStatsServiceImpl.userRoomPlayFlux(param));
    }


    @ApiOperation("查询用户观看记录列表")
    @GetMapping("/user/record")
    public ResponsePacket<PageResponse<LiveUserRecordVO>> userRecord(QueryRoomStatsParams param) {
        log.info("查询用户观看记录列表 param={}", param);
        return ResponsePacket.onSuccess(roomUserRecordServiceImpl.userRecord(param));
    }

    @ApiOperation("查询直播间流量明细数据")
    @GetMapping("/flux/record")
    public ResponsePacket<PageResponse<LiveRoomStatsVO>> roomFluxDetail(QueryRoomStatsParams param) {
        log.info("查询直播间流量明细数据 param={}", param);
        return ResponsePacket.onSuccess(roomInfoRecordServiceImpl.roomFluxDetail(param));
    }

    @ApiOperation("查询最近三分钟直播数据")
    @GetMapping("/three/record")
    public ResponsePacket<List<LiveRoomStatsVO>> threeSecondRoomRecord(RoomNoParam param) {
        log.info("查询最近三分钟直播数据 param={}", param);
        return ResponsePacket.onSuccess(roomInfoRecordServiceImpl.threeSecondRoomRecord(param));
    }



    @ApiOperation("查询用户观看数据统计列表（待开发）")
    @GetMapping("/user/stats")
    public ResponsePacket<PageResponse<LiveUserStatsVO>> userStats(QueryRoomStatsParams param) {
        log.info("查询用户观看数据统计列表 param={}", param);
        /*return ResponsePacket.onSuccess(roomUserRecordServiceImpl.userStats(param));*/
        return ResponsePacket.onSuccess();
    }

//    @ApiOperation("查询多个历史房间流量信息")
//    @GetMapping("/roomNos")
//    public ResponsePacket<Boolean> getHisRoomFluxInfo(@RequestParam("roomNos") String roomNos,
//                                                                        @RequestParam("apiPwd") String apiPwd) {
//        if(StringUtils.isBlank(apiPwd) ||
//                !StringUtils.equals(DateUtil.now().toString()+"dbpick-stats-LiveStatsController-getHisRoomFluxInfo",apiPwd)){
//            return ResponsePacket.onError();
//        }
//        return ResponsePacket.onSuccess(roomFluxStatsServiceImpl.roomFluxStatsServiceImpl(roomNos));
//    }


}
