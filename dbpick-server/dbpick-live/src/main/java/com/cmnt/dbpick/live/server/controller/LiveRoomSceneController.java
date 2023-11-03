package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.enums.SwitchEnum;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.live.api.params.LiveRoomSceneParam;
import com.cmnt.dbpick.live.api.params.LiveRoomSceneSwitchParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.vo.LiveRoomSceneVO;
import com.cmnt.dbpick.live.server.service.LiveRoomSceneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Api(tags = {"直播现场设置相关接口"})
@RequestMapping("/room/scene")
@RestController
public class LiveRoomSceneController extends BaseController {

    @Autowired
    private LiveRoomSceneService liveRoomSceneServiceImpl;

    @ApiOperation("添加直播现场")
    @PostMapping("/add")
    public ResponsePacket<LiveRoomSceneVO> addScene(@Validated @RequestBody LiveRoomSceneParam param) {
        getParamAccess(param);
        log.info("添加直播现场 param={}", param);
        return ResponsePacket.onSuccess(liveRoomSceneServiceImpl.addScene(param));
    }


    @ApiOperation("直播现场数据分页列表(管理端)")
    @GetMapping("/list")
    public ResponsePacket<PageResponse<LiveRoomSceneVO>> getScenePageList(StreamingRoomQueryParams param){
        getParamAccess(param);
        log.info("查询直播现场数据列表 param={}", param);
        return ResponsePacket.onSuccess(liveRoomSceneServiceImpl.getScenePageList(param));
    }


    @ApiOperation("更新上下架状态")
    @PostMapping("/status")
    public ResponsePacket<LiveRoomSceneVO> updateSceneStatus(@RequestBody LiveRoomSceneSwitchParam param) {
        getParamAccess(param);
        log.info("更新上下架状态 param={}", param);
        LiveRoomSceneVO vo = liveRoomSceneServiceImpl.updateSceneStatus(param);

        long nowSeconds = DateUtil.getTimeStrampSeconds();
        log.info("更新上下架状态 vo={}, nowSeconds={}", nowSeconds);
        if(StringUtils.equals(vo.getStatusSwitch(), SwitchEnum.OPEN.getValue())
                && DateUtil.parseYMDHMS2Mils(vo.getOverTime())>=nowSeconds){
            RoomNoParam roomNoParam = new RoomNoParam();
            roomNoParam.setRoomNo(vo.getRoomNo());
            liveRoomSceneServiceImpl.asyncSendImSceneMsg(roomNoParam);
        }
        return ResponsePacket.onSuccess(vo);
    }


    @ApiOperation("可用的直播现场列表数据")
    @GetMapping("/useful/list")
    public ResponsePacket<List<LiveRoomSceneVO>> usefulList(RoomNoParam param) {
        log.info("查询可用的直播现场列表数据 param={}", param);
        return ResponsePacket.onSuccess(liveRoomSceneServiceImpl.usefulList(param));
    }

}
