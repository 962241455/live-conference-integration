package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.live.api.params.RoomWatchFilterParam;
import com.cmnt.dbpick.live.api.params.RoomWatchFilterRegisterParam;
import com.cmnt.dbpick.live.api.vo.WatchRegisterInfoVO;
import com.cmnt.dbpick.live.server.service.WatchFilterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api(tags = {"观看限制接口"})
@RequestMapping("/watch/filter")
@RestController
public class WatchFilterController extends BaseController {

    @Autowired
    private WatchFilterService watchFilterServiceImpl;

    @ApiOperation("取消直播观看限制")
    @PostMapping("/cancel")
    public ResponsePacket<Boolean> cancelFilter(@Validated @RequestBody RoomWatchFilterParam param) {
        getParamAccess(param);
        log.info("取消直播间观看限制 param={}", param);
        return ResponsePacket.onSuccess(watchFilterServiceImpl.cancelFilter(param));
    }

    @ApiOperation("关闭游客观看")
    @PostMapping("/close")
    public ResponsePacket<Boolean> closeVisitorWatch(@Validated @RequestBody RoomWatchFilterParam param) {
        getParamAccess(param);
        log.info("关闭游客观看 param={}", param);
        return ResponsePacket.onSuccess(watchFilterServiceImpl.closeVisitorWatch(param));
    }


    @ApiOperation("保存直播观看限制")
    @PostMapping("/save")
    public ResponsePacket<Boolean> saveFilter(@Validated @RequestBody RoomWatchFilterRegisterParam param) {
        getParamAccess(param);
        log.info("保存直播观看限制 param={}", param);
        return ResponsePacket.onSuccess(watchFilterServiceImpl.saveFilter(param));
    }


    @ApiOperation("查询直播观看限制")
    @PostMapping("/info")
    public ResponsePacket<WatchRegisterInfoVO> findFilter(@Validated @RequestBody RoomWatchFilterParam param) {
        //getParamAccess(param);
        log.info("查询直播观看限制 param={}", param);
        return ResponsePacket.onSuccess(watchFilterServiceImpl.findFilter(param));
    }

}
