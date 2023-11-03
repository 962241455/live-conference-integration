package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.RoomWarmInfoVO;
import com.cmnt.dbpick.live.server.service.RoomWarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 暖场视频
 */
@Slf4j
@Api(tags = {"后管-暖场视频 接口"})
@RequestMapping("/warm")
@RestController
public class RoomWarmController extends BaseController {

    @Autowired
    private RoomWarmService roomWarmServiceImpl;

    @ApiOperation("暖场视频信息")
    @GetMapping("/info")
    public ResponsePacket<RoomWarmInfoVO> detail(StreamingRoomQueryParams param) {
        getParamAccess(param);
        log.info("查询暖场视频信息 param={}", param);
        return ResponsePacket.onSuccess(roomWarmServiceImpl.info(param));
    }

    @ApiOperation("保存暖场视频信息")
    @PostMapping("/save")
    public ResponsePacket<RoomWarmInfoVO> saveWarmVideo(@Validated @RequestBody RoomVideoEditParam param) {
        getParamAccess(param);
        log.info("保存暖场视频信息 param={}", param);
        return ResponsePacket.onSuccess(roomWarmServiceImpl.saveWarmVideo(param));
    }

    @ApiOperation("删除暖场视频信息")
    @DeleteMapping("/delete/{id}")
    public ResponsePacket<Boolean> deleteWarmVideo(@PathVariable("id") String id) {
        log.info("删除暖场视频信息 id={}", id);
        if(StringUtils.isBlank(id)){
            log.error("视频id 不能为空");
            throw new BizException("视频id 不能为空");
        }
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(roomWarmServiceImpl.deleteWarmVideo(id,accessToken.getUserId()));
    }


    @ApiOperation("编辑暖场视频状态")
    @PostMapping("/status")
    public ResponsePacket<RoomWarmInfoVO> editWarmVideoStatus(@Validated @RequestBody RoomVideoEditParam param) {
        getParamAccess(param);
        log.info("编辑暖场视频状态 param={}", param);
        return ResponsePacket.onSuccess(roomWarmServiceImpl.editWarmVideoStatus(param));
    }


}
