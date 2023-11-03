package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.user.StreamingRoomTimeVO;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.live.api.message.RoomPlayRecordMessage;
import com.cmnt.dbpick.live.api.params.RoomSilencedParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomParams;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.live.api.vo.RoomHotOnlineVO;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author
 */
@Slf4j
@Api(tags = {"直播间接口"})
@RequestMapping("/streaming/room")
@RestController
public class StreamingRoomController extends BaseController {

    @Autowired
    private StreamingRoomService streamingRoomService;

    @ApiOperation("新增直播房间")
    @PostMapping("/add")
    public ResponsePacket<StreamingRoomVO> add(@Validated @RequestBody StreamingRoomParams params) {
        getParamAccess(params);
        return ResponsePacket.onSuccess(streamingRoomService.add(params));
    }

    @ApiOperation("更新直播房间")
    @PutMapping("/{id}")
    public ResponsePacket<StreamingRoomVO> update(@Validated @RequestBody StreamingRoomParams params) {
        if(Objects.isNull(params) || StringUtils.isBlank(params.getId())){
            log.error("房间id不能为空 params={}", params);
            throw new BizException("房间id不能为空");
        }
        getParamAccess(params);
        return ResponsePacket.onSuccess(streamingRoomService.update(params));
    }

    @ApiOperation("删除直播房间")
    @DeleteMapping("/delete/{id}")
    public ResponsePacket<String> deleteRoom(
            @PathVariable(value = "id",required = false) String id) {
        /*if(StringUtils.isBlank(id)){
            log.error("房间id 不能为空");
            throw new BizException("房间id 不能为空");
        }
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(streamingRoomService.deleteById(id,accessToken.getUserId()));*/
        return ResponsePacket.onSuccess("暂不支持删除直播房间");
    }

    @ApiOperation("关闭直播间")
    @GetMapping("/drop/{roomNo}")
    public ResponsePacket<Boolean> dropRoom(@PathVariable("roomNo") String roomNo) {
        if(StringUtils.isBlank(roomNo)){
            log.error("房间号 不能为空");
            throw new BizException("房间号 不能为空");
        }
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(streamingRoomService.dropRoom(roomNo,accessToken.getUserId()));
    }

    @ApiOperation("启动直播间")
    @GetMapping("/resume/{roomNo}")
    public ResponsePacket<Boolean> resumeRoom(@PathVariable("roomNo") String roomNo) {
        if(StringUtils.isBlank(roomNo)){
            log.error("房间号 不能为空");
            throw new BizException("房间号 不能为空");
        }
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(streamingRoomService.resumeRoom(roomNo,accessToken.getUserId()));
    }

    @ApiOperation("直播间禁流(tx端)")
    @GetMapping("/forbid/{streamName}")
    public ResponsePacket forbidLiveStream(@PathVariable("streamName") String streamName) {
        if(StringUtils.isBlank(streamName)){
            log.error("流名称 不能为空");
            throw new BizException("流名称 不能为空");
        }
        TokenInfo accessToken = getAccessToken();
        log.info("直播间禁流(tx端) ,操作人：{}",accessToken.getUserId());
        streamingRoomService.forbidLiveStream(streamName);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation("直播房间列表")
    @GetMapping("/list/simple")
    public ResponsePacket<PageResponse<StreamingRoomVO>> list(StreamingRoomQueryParams params) {
        log.info("查询直播房间列表 params={}", params);
        getParamAccess(params);
        PageResponse<StreamingRoomVO> list = streamingRoomService.list(params);
        return ResponsePacket.onSuccess(list);
    }

    @PostMapping("/list/third")
    public ResponsePacket<List<String>> listByThird(@RequestBody ThirdRoomQueryParam param) {
        log.info("查询用户所有直播房间列表 params={}", param);
        if(StringUtils.isBlank(param.getThirdId())){
            throw new BizException("请输入用户账号信息");
        }
        List<String> roomList = streamingRoomService.listByThird(param);
        return ResponsePacket.onSuccess(roomList);
    }

    @ApiOperation("根据id查询直播间详情")
    @GetMapping("/info/{id}")
    public ResponsePacket<StreamingRoomVO> info(@PathVariable("id") String id) {
        return ResponsePacket.onSuccess(streamingRoomService.info(id));
    }

    @ApiOperation("更新房间推流地址")
    @GetMapping("/refresh/{roomNo}")
    public ResponsePacket<StreamingRoomVO> refreshRoomPushUrl(@PathVariable("roomNo") String roomNo) {
        return ResponsePacket.onSuccess(streamingRoomService.refreshRoomPushUrl(roomNo));
    }

    @ApiOperation("开启三方推流")
    @GetMapping("/open/{roomNo}")
    public ResponsePacket<String> openThirdPush(@PathVariable("roomNo") String roomNo) {
        //return ResponsePacket.onSuccess(streamingRoomService.openThirdPush(roomNo));
        return ResponsePacket.onSuccess("暂不支持");
    }

    @ApiOperation("强制开启直播")
    @GetMapping("/living/{roomNo}")
    public ResponsePacket<Boolean> startRoomLiveIng(@PathVariable("roomNo") String roomNo) {
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(streamingRoomService.startRoomLiveIng(roomNo,accessToken.getUserId()));
    }

    @ApiOperation("直播房间详情")
    @GetMapping("/detail")
    public ResponsePacket<StreamingRoomVO> detail(@RequestParam("roomNo") String roomNo) {
        return ResponsePacket.onSuccess(streamingRoomService.detail(roomNo));
    }

    @ApiOperation("创建群组")
    @GetMapping("/create/group")
    public ResponsePacket<Boolean> createImGroup(@RequestParam("roomNo") String roomNo,
                                                 @RequestParam(value = "userId", required = false) String userId) {
        TokenInfo accessToken = getAccessToken();
        log.info("创建群组 ,操作人：{}",accessToken.getUserId());
        return ResponsePacket.onSuccess(streamingRoomService.createChatRoom(roomNo,userId));
    }
    @ApiOperation("解散群组")
    @GetMapping("/des/group")
    public ResponsePacket<Boolean> destroyImGroup(@RequestParam("roomNo") String roomNo) {
        TokenInfo accessToken = getAccessToken();
        log.info("解散群组 ,操作人：{}",accessToken.getUserId());
        return ResponsePacket.onSuccess(streamingRoomService.destroyChatRoom(roomNo));
    }
    /**
     * 禁言房间
     */
    @ApiOperation("禁言房间")
    @PostMapping("/silenced")
    public ResponsePacket<Boolean> silencedChatRoom(@Validated @RequestBody RoomSilencedParam param) {
        getParamAccess(param);
        log.info("禁言房间设置 param={}", param);
        return ResponsePacket.onSuccess(streamingRoomService.silencedChatRoom(param));
    }



    @ApiOperation("更新直播间热度和在线人数")
    @PostMapping("/hot/online")
    public ResponsePacket updRoomHotAndOnline(@RequestBody RoomHotOnlineVO param) {
        return ResponsePacket.onSuccess(streamingRoomService.updRoomHotAndOnline(param));
    }

    @ApiOperation("查询用户热度值")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name="access_token",value = "access_token",required = true)
    })
    @GetMapping("/hot/user")
    public ResponsePacket<String> getThirdRedisHot() {
        TokenInfo accessToken = getAccessToken();
        log.info("查询用户热度值：{}",accessToken.getUserId());
        return ResponsePacket.onSuccess(streamingRoomService.getThirdRedisHot(accessToken.getUserId()));
    }
    @ApiOperation("初始化用户热度值")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name="access_token",value = "access_token",required = true)
    })
    @GetMapping("/hot/init")
    public ResponsePacket<String> initThirdRedisHot() {
        TokenInfo accessToken = getAccessToken();
        log.info("初始化用户热度值：{}",accessToken.getUserId());
        return ResponsePacket.onSuccess(streamingRoomService.initThirdRedisHot(accessToken.getUserId()));
    }


    @ApiOperation("查询多个房间信息")
    @GetMapping("/roomNos")
    public ResponsePacket<List<StreamingRoomTimeVO>> getInfoByRoomNos(@RequestParam("roomNos") String roomNos,
                                                                      @RequestParam("apiPwd") String apiPwd) {
        if(StringUtils.isBlank(apiPwd) ||
                !StringUtils.equals(DateUtil.now().toString()+"dbpick-live-StreamingRoomController-getInfoByRoomNos",apiPwd)){
            return ResponsePacket.onError();
        }
        List<StreamingRoomTimeVO> infoByRoomNos = streamingRoomService.getInfoByRoomNos(roomNos);
        log.info("查询多个房间信息 ,infoByRoomNos：{}",infoByRoomNos);
        return ResponsePacket.onSuccess(infoByRoomNos);
    }

    @ApiOperation("stats")
    @PostMapping("/stats/mq")
    public ResponsePacket statsMq(@RequestBody RoomPlayRecordMessage params) {
        streamingRoomService.sendRoomPlayRecordMessage(params);
        return ResponsePacket.onSuccess();
    }


}
