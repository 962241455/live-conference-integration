package com.cmnt.dbpick.third.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.third.api.params.MerchantPackageEditParam;
import com.cmnt.dbpick.third.api.params.MerchantPackageQueryParam;
import com.cmnt.dbpick.third.api.vo.MerchantPackageVo;
import com.cmnt.dbpick.third.server.service.ThirdMerchantPackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 第三方商户套餐管理
 */
@Slf4j
@Api(tags = "中台管理 - 商户套餐")
@RestController
@RequestMapping("/merchantPackage")
public class ThirdMerchantPackageController extends BaseController {

    @Autowired
    private ThirdMerchantPackageService thirdMerchantPackageService;


    @ApiOperation("获取商户套餐列表")
    @GetMapping("/list")
    public ResponsePacket<PageResponse<MerchantPackageVo>> getMerchantPackageList(MerchantPackageQueryParam param) {
        PageResponse<MerchantPackageVo> merchantPackageVo = thirdMerchantPackageService.getMerchantPackageList(param);
        return ResponsePacket.onSuccess(merchantPackageVo);
    }

    @ApiOperation("生成商户套餐")
    @PostMapping("/create")
    public ResponsePacket<MerchantPackageVo> createMerchantPackage(@Validated @RequestBody MerchantPackageEditParam param) {
        MerchantPackageVo merchantPackageVo = thirdMerchantPackageService.createMerchantPackage(param);
        return ResponsePacket.onSuccess(merchantPackageVo);
    }

    @ApiOperation("修改商户套餐")
    @PostMapping("/update")
    public ResponsePacket<MerchantPackageVo> updateMerchantPackage(@Validated @RequestBody MerchantPackageEditParam param) {
        MerchantPackageVo merchantPackageVo = thirdMerchantPackageService.updateMerchantPackage(param);
        return ResponsePacket.onSuccess(merchantPackageVo);
    }

    @ApiOperation("删除商户套餐")
    @PostMapping("/del")
    public ResponsePacket delMerchantPackage(@RequestParam("id") String id) {
        thirdMerchantPackageService.delMerchantPackage(id);
        return ResponsePacket.onSuccess();
    }
}
