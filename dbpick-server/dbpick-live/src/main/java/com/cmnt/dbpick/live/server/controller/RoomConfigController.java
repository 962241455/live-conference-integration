package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import com.cmnt.dbpick.common.user.RoomConfigVO;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.live.api.params.RoomConfigEditParam;
import com.cmnt.dbpick.live.api.vo.TxCosConfigVo;
import com.cmnt.dbpick.live.server.service.RoomConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */
@Slf4j
@Api(tags = {"后管-直播间通用配置接口"})
@RequestMapping("/room/config")
@RestController
public class RoomConfigController extends BaseController {

    @Autowired
    private RoomConfigService roomConfigService;

    @ApiOperation("查询直播间默认配置")
    @GetMapping("/info")
    public ResponsePacket<RoomConfigVO> list(ThirdAccessKeyInfo param) {
        log.info("查询直播间默认配置 params={}", param);
        getParamAccess(param);
        RoomConfigVO info = roomConfigService.info(param);
        return ResponsePacket.onSuccess(info);
    }

    @ApiOperation("编辑直播间默认配置")
    @PostMapping("/edit")
    public ResponsePacket<RoomConfigVO> add(@Validated @RequestBody RoomConfigEditParam param) {
        getParamAccess(param);
        log.info("编辑直播间默认配置 params={}", param);
        return ResponsePacket.onSuccess(roomConfigService.edit(param));
    }



    @ApiOperation("获取文件最大size (fileType=video,单位 GB; fileType=image,单位 MB;)")
    @GetMapping("/max/size")
    public ResponsePacket<Integer> getFileMaxSize(
            @ApiParam(value = "文件类型： video-视频， image-图片", required = true)
            @RequestParam("fileType") String fileType){
        log.info("获取文件最大size currentTime={}", DateUtil.getTimeStrampSeconds());
        return ResponsePacket.onSuccess(roomConfigService.getFileMaxSize(fileType));
    }

    @ApiOperation("获取cos配置信息")
    @GetMapping("/cos")
    public ResponsePacket<TxCosConfigVo> getCosInfo(){
        TokenInfo accessToken = getAccessToken();
        log.info("获取cos配置信息 currentTime={}, user={}", DateUtil.getTimeStrampSeconds(),accessToken.getUserId());
        return ResponsePacket.onSuccess(roomConfigService.getCosInfo(accessToken.getUserId()));
    }


}
