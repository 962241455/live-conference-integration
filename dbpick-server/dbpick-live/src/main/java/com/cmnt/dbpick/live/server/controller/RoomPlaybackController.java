package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.RoomPlaybackInfoVO;
import com.cmnt.dbpick.live.api.vo.RoomPlaybackVO;
import com.cmnt.dbpick.live.server.service.RoomPlaybackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Api(tags = {"直播间回放接口"})
@RequestMapping("/room/playback")
@RestController
public class RoomPlaybackController extends BaseController {

    @Autowired
    private RoomPlaybackService roomPlaybackServiceImpl;

    /**
     * 房间回放列表
     */
    @ApiOperation("房间回放列表")
    @GetMapping("/list")
    public ResponsePacket<PageResponse<RoomPlaybackVO>> list(StreamingRoomQueryParams params) {
        log.info("查询房间回放列表 params={}", params);
        getParamAccess(params);
        PageResponse<RoomPlaybackVO> list = roomPlaybackServiceImpl.list(params);
        return ResponsePacket.onSuccess(list);
    }

    @ApiOperation("编辑回放配置")
    @PostMapping("/edit")
    public ResponsePacket<Boolean> editPlayback(@Validated @RequestBody RoomPlaybackParam param) {
        getParamAccess(param);
        log.info("编辑回放配置 param={}", param);
        return ResponsePacket.onSuccess(roomPlaybackServiceImpl.editPlayback(param));
    }


    @ApiOperation("商户端添加回放视频")
    @PostMapping("/push/videos")
    public ResponsePacket<Boolean> pushVideos(@RequestBody RoomPlaybackVideosParam videosParam) {
        getParamAccess(videosParam);
        log.info("商户端添加回放视频 param={}", videosParam);
        return ResponsePacket.onSuccess(roomPlaybackServiceImpl.pushVideos(videosParam));
    }


    @ApiOperation("编辑回放视频名称")
    @PostMapping("/edit/playName")
    public ResponsePacket<RoomPlaybackVO> editPlayName(@RequestBody RoomPlaybackNameParam param) {
        getParamAccess(param);
        log.info("编辑回放视频名称 param={}", param);
        return ResponsePacket.onSuccess(roomPlaybackServiceImpl.editPlayName(param));
    }

    @ApiOperation("编辑回放视频顺序")
    @PostMapping("/edit/playSort")
    public ResponsePacket<Boolean> editPlaySort(@RequestBody RoomPlaybackSortParam param) {
        getParamAccess(param);
        log.info("编辑回放视频顺序 param={}", param);
        return ResponsePacket.onSuccess(roomPlaybackServiceImpl.editPlaySort(param));
    }

    @ApiOperation("删除回放视频视频")
    @DeleteMapping("/delete/{id}")
    public ResponsePacket<Boolean> deletePlaybackVideo(@PathVariable("id") String id) {
        if(StringUtils.isBlank(id)){
            log.error("回放视频id 不能为空");
            throw new BizException("回放视频id 不能为空");
        }
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(roomPlaybackServiceImpl.deletePlaybackVideo(id, accessToken.getUserId()));
    }


    @ApiOperation("查询房间回放信息")
    @GetMapping("/list/top10")
    public ResponsePacket<RoomPlaybackInfoVO> listTop10(RoomPlaybackQueryParam params) {
        log.info("查询房间回放信息 params={}", params);
        RoomPlaybackInfoVO vo = roomPlaybackServiceImpl.listTop10(params);
        return ResponsePacket.onSuccess(vo);
    }

}
