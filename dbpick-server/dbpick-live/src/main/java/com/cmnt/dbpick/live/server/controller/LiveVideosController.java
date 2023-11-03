package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.live.api.params.LiveVideosQueryParams;
import com.cmnt.dbpick.live.api.params.RoomVideoEditParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.params.VideoPushRoomParam;
import com.cmnt.dbpick.live.api.vo.RoomVideoLivesVO;
import com.cmnt.dbpick.live.api.vo.RoomVideoVO;
import com.cmnt.dbpick.live.server.service.LiveVideosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * @author
 */
@Slf4j
@Api(tags = {"后管-视频相关接口"})
@RequestMapping("/videos")
@RestController
public class LiveVideosController extends BaseController {

    @Autowired
    private LiveVideosService liveVideosService;

    @ApiOperation("视频库列表")
    @GetMapping("/list")
    public ResponsePacket<PageResponse<RoomVideoVO>> videoList(LiveVideosQueryParams param){
        getParamAccess(param);
        log.info("查询视频库列表 param={}", param);
        return ResponsePacket.onSuccess(liveVideosService.getVideoList(param));
    }

    @ApiOperation("获取云点播视频上传签名")
    @GetMapping("/getSign")
    public ResponsePacket getVodUploadSign(){
        log.info("获取云点播视频上传签名 currentTime={}", DateUtil.getTimeStrampSeconds());
        return ResponsePacket.onSuccess(liveVideosService.getVodUploadSign());
    }

    @ApiOperation("获取文件最大size (单位 GB)")
    @GetMapping("/max/size")
    public ResponsePacket<Integer> getFileMaxSize(){
        log.info("获取文件最大size currentTime={}", DateUtil.getTimeStrampSeconds());
        return ResponsePacket.onSuccess(liveVideosService.getFileMaxSize());
    }



    @ApiOperation("保存上传视频库成功的视频信息")
    @PostMapping("/save")
    public ResponsePacket<Boolean> saveVideoInfo(@Validated @RequestBody RoomVideoEditParam param) {
        getParamAccess(param);
        log.info("保存上传视频库成功的视频信息 param={}", param);
        return ResponsePacket.onSuccess(liveVideosService.saveVodVideo(param));
    }

    @ApiOperation("删除视频库视频")
    @DeleteMapping("/delete/{id}")
    public ResponsePacket<Boolean> deleteVideo(@PathVariable("id") String id) {
        if(StringUtils.isBlank(id)){
            log.error("视频id 不能为空");
            throw new BizException("id 不能为空");
        }
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(liveVideosService.deleteVideo(id, accessToken.getUserId()));
    }
    @ApiOperation("删除未保存视频库视频")
    @DeleteMapping("/deleteFile/{fileId}")
    public ResponsePacket<Boolean> deleteVideoByFileId(@PathVariable("fileId") String fileId) {
        if(StringUtils.isBlank(fileId)){
            log.error("视频 fileId 为空");
            return ResponsePacket.onSuccess();
        }
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(liveVideosService.deleteVideoByFileId(fileId, accessToken.getUserId()));
    }

    @ApiOperation("对视频库视频进行转码")
    @PostMapping("/transcode")
    public ResponsePacket<RoomVideoVO> transcodeVideo(@Validated @RequestBody RoomVideoEditParam param) {
        getParamAccess(param);
        log.info("对视频库视频进行转码 param={}", param);
        return ResponsePacket.onSuccess(liveVideosService.transcodeVideo(param));
    }



    @ApiOperation("视频录播任务列表")
    @GetMapping("/task/list")
    public ResponsePacket<PageResponse<RoomVideoLivesVO>> videoTaskLive(StreamingRoomQueryParams param) {
        getParamAccess(param);
        log.info("查询视频录播任务列表 param={}", param);
        return ResponsePacket.onSuccess(liveVideosService.videoTaskLive(param));
    }


    @ApiOperation("添加/编辑视频录播任务")
    @PostMapping("/task/edit")
    public ResponsePacket<String> editVideoTask(@Validated @RequestBody VideoPushRoomParam param) {
        getParamAccess(param);
        log.info("添加/编辑视频录播任务 param={}", param);
        return ResponsePacket.onSuccess(liveVideosService.editVideoTask(param));
    }

    @ApiOperation("删除视频录播任务")
    @DeleteMapping("/task/delete/{id}")
    public ResponsePacket<Boolean> deleteVideoTask(@PathVariable("id") String id) {
        if(StringUtils.isBlank(id)){
            log.error("视频任务id 不能为空");
            throw new BizException("视频任务id 不能为空");
        }
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(liveVideosService.deleteVideoTask(id, accessToken.getUserId()));
    }



}
