package com.cmnt.dbpick.stats.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.StringUtils;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.stats.api.params.AnalyseWatchTimesParam;
import com.cmnt.dbpick.stats.api.params.QueryRoomStatsParams;
import com.cmnt.dbpick.stats.api.vo.es.RoomUserRecordStatsNo;
import com.cmnt.dbpick.stats.server.service.WatchTimesStatsService;
import com.cmnt.dbpick.stats.server.utils.StreamingRoomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


@Slf4j
@Api(tags = {"直播观看时长数据相关接口"})
@RequestMapping("/watch/record")
@RestController
public class WatchTimesStatsController extends BaseController{

    @Resource
    private HttpServletRequest request;

    @Autowired
    private WatchTimesStatsService watchTimesStatsServiceImpl;
    @Autowired
    private StreamingRoomUtil streamingRoomUtil;

    @ApiOperation("数据分析")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name="accesskey",value = "accesskey",required = true)
    })
    @GetMapping("/analyse")
    public ResponsePacket analyseWatchTimes(String roomNo) {
        String ak = request.getHeader("accesskey");
        log.info("accesskey 获取到值为： {}", ak);
        if (StringUtils.isBlank(ak)) {
            throw new BizException(ResponseCode.GET_TOKEN_ERROR);
        }
        watchTimesStatsServiceImpl.analyseWatchTimes(roomNo,ak);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation("分页查询用户观看时长")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name="accesskey",value = "accesskey",required = true)
    })
    @GetMapping("/stats")
    public ResponsePacket<PageResponse<RoomUserRecordStatsNo>> stats(StreamingRoomQueryParams param) {
        String ak = request.getHeader("accesskey");
        log.info("accesskey 获取到值为： {}", ak);
        if (StringUtils.isBlank(ak)) {
            throw new BizException(ResponseCode.GET_TOKEN_ERROR);
        }
        log.info("分页查询用户观看时长 param={}", param);
        return ResponsePacket.onSuccess(watchTimesStatsServiceImpl.stats(param,ak));
    }




    private void checkTokenWithRoomNo(String roomNo){
        TokenInfo accessToken = getAccessToken();
        StreamingRoomVO roomVO = streamingRoomUtil.getByRoomNoAndRefreshRedis(roomNo);
        if (Objects.isNull(roomVO)){
            throw new BizException(ResponseCode.AUCTION_ROOM_NULL);
        }
        if(!StringUtils.equals(roomVO.getThirdId(),accessToken.getThirdId())){
            throw new BizException(ResponseCode.NOT_FIND_ACCESS);
        }
    }
    @ApiOperation("数据分析-管理端")
    @GetMapping("/analyseMgr")
    public ResponsePacket<Boolean> analyseThird(AnalyseWatchTimesParam param) {
        log.info("es数据分析-管理端 param={}",param);
        checkTokenWithRoomNo(param.getRoomNo());
        return ResponsePacket.onSuccess(watchTimesStatsServiceImpl.analyseWatchTimes(param));
    }

    @ApiOperation("查询分析完成的数据-管理端")
    @GetMapping("/statsMgr")
    public ResponsePacket<PageResponse<RoomUserRecordStatsNo>> statsThird(QueryRoomStatsParams param) {
        log.info("查询分析完成的数据-管理端 param={}",param);
        checkTokenWithRoomNo(param.getRoomNo());
        try {
            return ResponsePacket.onSuccess(watchTimesStatsServiceImpl.stats(param));
        } catch (Exception e) {
            return ResponsePacket.onSuccess();
        }

    }



    /**
     * 导出观看时长
     */
    @ApiOperation("导出观看时长")
    @PostMapping(value = "/export",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public void exportVoteResult(String roomNo, HttpServletResponse response) {
        checkTokenWithRoomNo(roomNo);
        try {
            watchTimesStatsServiceImpl.exportStatsData(roomNo,response);
        }catch (Exception e){
            log.error("导出观看时长失败:{ }",e);
            throw new BizException("网络波动，请稍后重试");
        }

    }



}
