package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.LivePornNoticeQueryParam;
import com.cmnt.dbpick.live.api.vo.LivePornNoticeVO;
import com.cmnt.dbpick.live.server.service.LivePornNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api(tags = {"鉴黄通知相关接口"})
@RequestMapping("/porn")
@RestController
public class LivePornNoticeController extends BaseController {

    @Autowired
    private LivePornNoticeService livePornNoticeServiceImpl;


    @ApiOperation("查询鉴黄通知信息")
    @GetMapping("/list")
    public ResponsePacket<PageResponse<LivePornNoticeVO>> list(LivePornNoticeQueryParam param) {
        log.info("查询鉴黄通知信息 param={}", param);
        getParamAccess(param);
        PageResponse<LivePornNoticeVO> list = livePornNoticeServiceImpl.list(param);
        return ResponsePacket.onSuccess(list);
    }



}
