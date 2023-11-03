package com.cmnt.dbpick.stats.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.utils.RedisUtils;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.stats.api.params.ThirdVodPlaybackQueryParam;
import com.cmnt.dbpick.stats.api.vo.SettlementPlaybackVO;
import com.cmnt.dbpick.stats.api.vo.ThirdVodPlaybackVO;
import com.cmnt.dbpick.stats.server.service.PlaybackVodFluxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;


@Slf4j
@Api(tags = {"回放流量相关接口"})
@RequestMapping("/playback")
@RestController
public class PlaybackVodController extends BaseController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private PlaybackVodFluxService playbackVodFluxServiceImpl;

    @ApiOperation("初始化指定时间内的回放流量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name="access_token",value = "access_token",required = true)
    })
    @GetMapping("/init/flux")
    public ResponsePacket<SettlementPlaybackVO> thirdPlaybackFlux(ThirdRoomQueryParam param) {
        log.info("初始化指定时间内的回放流量 param={}", param);
        getParamAccess(param);
        String thirdId = param.getThirdId();
        Object adminUser = redisUtils.get(RedisKey.SYS_THIRD_USER_ADMIN);
        if(Objects.isNull(adminUser) || StringUtils.isBlank(thirdId) ||
                !StringUtils.equals(thirdId,String.valueOf(adminUser))){
            throw new BizException("请使用管理员账号进行操作");
        }
        playbackVodFluxServiceImpl.initPlaybackVideoFlux(
                param.getSearchStartTime(),param.getSearchEndTime());
        return ResponsePacket.onSuccess();
    }


    @ApiOperation("查询vod视频回放详情列表")
    @GetMapping("/file/detail")
    public ResponsePacket<List<ThirdVodPlaybackVO>> getVodFilePlaybackFlux(ThirdVodPlaybackQueryParam param) {
        log.info("查询vod视频回放详情列表 param={}", param);
        getParamAccess(param);
        if(StringUtils.isBlank(param.getThirdId())){
            throw new BizException("请输入用户账号信息");
        }
        return ResponsePacket.onSuccess(playbackVodFluxServiceImpl.getVodFilePlaybackFlux(param));
    }






}
