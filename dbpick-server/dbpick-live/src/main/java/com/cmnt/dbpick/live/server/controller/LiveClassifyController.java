/**
 * Demo class
 *
 * @author 28021
 * @date 2022/8/11
 */
package com.cmnt.dbpick.live.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.third.GmLiveClassIfyParams;
import com.cmnt.dbpick.live.api.vo.third.LiveClassifyVo;
import com.cmnt.dbpick.live.server.mongodb.document.LiveClassify;
import com.cmnt.dbpick.live.server.service.LiveClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理
 *
 * @author mr . wei
 * @date 2022/8/11
 */
@Slf4j
@Api(tags = {"后管-分类管理"})
@RequestMapping("/liveClassify")
@RestController
public class LiveClassifyController extends BaseController {


    @Autowired
    LiveClassifyService liveClassifyService;


    @ApiOperation("添加分类")
    @PostMapping("/add")
    public ResponsePacket add(@Validated @RequestBody LiveClassify params) throws Exception {
        TokenInfo accessToken = getAccessToken();
        log.info("添加分类列表 params={}", params);
        boolean save = liveClassifyService.save(params);
        return ResponsePacket.onSuccess(save);
    }

    @ApiOperation("分类列表")
    @GetMapping("/list")
    public ResponsePacket<PageResponse<LiveClassifyVo>> list(GmLiveClassIfyParams params)throws Exception {
        log.info("查询分类列表 params={}", params);
        TokenInfo accessToken = getAccessToken();
        return ResponsePacket.onSuccess(liveClassifyService.getLiveClassifyList(params));
    }


    @ApiOperation("删除分类")
    @DeleteMapping("/isDeletel/{id}")
    public ResponsePacket isDeletel(@PathVariable("id") String id)throws Exception{
        return ResponsePacket.onSuccess(liveClassifyService.isDeletel(id));
    }

    @ApiOperation("修改分类")
    @PostMapping("/upStatus")
    public ResponsePacket upStatus(@Validated @RequestBody LiveClassify params) throws Exception{

        boolean save = liveClassifyService.updataLiveClassify(params);
        return ResponsePacket.onSuccess();
    }
}
