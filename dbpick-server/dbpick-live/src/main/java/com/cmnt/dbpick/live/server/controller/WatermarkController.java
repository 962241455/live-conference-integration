package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.LiveRoomWatermarkParam;
import com.cmnt.dbpick.live.api.params.LiveWatermarkEditParam;
import com.cmnt.dbpick.live.api.params.LiveWatermarkQueryParams;
import com.cmnt.dbpick.live.api.vo.LiveWatermarkVO;
import com.cmnt.dbpick.live.server.service.WatermarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


/**
 * @author
 */
@Slf4j
@Api(tags = {"后管-水印管理接口"})
@RequestMapping("/watermark")
@RestController
public class WatermarkController extends BaseController {

    @Autowired
    private WatermarkService watermarkServiceImpl;

    @ApiOperation("水印图片列表")
    @GetMapping("/list")
    public ResponsePacket<PageResponse<LiveWatermarkVO>> videoList(LiveWatermarkQueryParams param){
        getParamAccess(param);
        log.info("查询水印图片列表 param={}", param);
        return ResponsePacket.onSuccess(watermarkServiceImpl.getList(param));
    }


    @ApiOperation("添加水印图片")
    @PostMapping("/add")
    public ResponsePacket<Boolean> add(@Validated @RequestBody LiveWatermarkEditParam param) {
        getParamAccess(param);
        log.info("添加水印图片 param={}", param);
        return ResponsePacket.onSuccess(watermarkServiceImpl.add(param));
    }


    @ApiOperation("更新水印图片")
    @PutMapping("/update/{id}")
    public ResponsePacket<LiveWatermarkVO> update(@Validated @RequestBody LiveWatermarkEditParam param) {
        if(Objects.isNull(param) || Objects.isNull(param.getId())){
            log.error("水印图片id不能为空 params={}", param);
            throw new BizException("水印图片id不能为空");
        }
        getParamAccess(param);
        log.info("更新水印图片 param={}", param);
        return ResponsePacket.onSuccess(watermarkServiceImpl.update(param));
    }


    @ApiOperation("删除水印图片")
    @DeleteMapping("/delete/{id}")
    public ResponsePacket<Boolean> deleteWatermark(@PathVariable("id") String id) {
        if(Objects.isNull(id)){
            log.error("水印id 不能为空");
            throw new BizException("水印id 不能为空");
        }
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(watermarkServiceImpl.deleteWatermark(id,accessToken.getUserId()));
    }


    @ApiOperation("添加直播间水印")
    @PostMapping("/create/live")
    public ResponsePacket<Boolean> createLive(@Validated @RequestBody LiveRoomWatermarkParam param) {
        getParamAccess(param);
        log.info("添加直播间水印 param={}", param);
        return ResponsePacket.onSuccess(watermarkServiceImpl.createLive(param));
    }

    @ApiOperation("取消直播间水印")
    @PostMapping("/cancel/live")
    public ResponsePacket<Boolean> cancelLive(@Validated @RequestBody LiveRoomWatermarkParam param) {
        getParamAccess(param);
        log.info("取消直播间水印 param={}", param);
        return ResponsePacket.onSuccess(watermarkServiceImpl.cancelLive(param));
    }

    @ApiOperation("取消所有使用改水印的直播间")
    @DeleteMapping("/cancel/all/{id}")
    public ResponsePacket<Boolean> cancelWatermarkAll(@PathVariable("id") String id) {
        if(Objects.isNull(id)){
            log.error("水印id 不能为空");
            throw new BizException("水印id 不能为空");
        }
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(watermarkServiceImpl.cancelWatermarkAll(id,accessToken.getUserId()));
    }



}
