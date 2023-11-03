package com.cmnt.dbpick.third.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.user.ThirdSysUserVO;
import com.cmnt.dbpick.third.api.params.EditStatusParam;
import com.cmnt.dbpick.third.api.params.SysThirdUserEditParam;
import com.cmnt.dbpick.third.api.params.SysThirdUserQueryParam;
import com.cmnt.dbpick.third.server.service.ThirdSysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
/**
 * 第三方商户账号管理
 */
@Slf4j
@Api(tags = "中台管理 - 第三方商户账号")
@RestController
@RequestMapping("/sys/user")
public class ThirdSysUserController extends BaseController {

    @Autowired
    private ThirdSysUserService thirdSysUserServiceImpl;

    @ApiOperation("生成商户账号")
    @PostMapping("/create")
    public ResponsePacket<ThirdSysUserVO> createSysThirdUser(@Validated @RequestBody SysThirdUserEditParam param) {
        TokenInfo accessToken = getAccessToken();
        param.setCreateUser(accessToken.getUserId());
        ThirdSysUserVO userVO = thirdSysUserServiceImpl.createSysThirdUser(param);
        return ResponsePacket.onSuccess(userVO);
    }

    @ApiOperation("获取第三方商户账号列表")
    @GetMapping("/list")
    public ResponsePacket<PageResponse<ThirdSysUserVO>> getSysThirdUserList(SysThirdUserQueryParam params) {
        log.info("获取商户账号列表 params={}", params);
        PageResponse<ThirdSysUserVO> list = thirdSysUserServiceImpl.getSysThirdUserList(params);
        return ResponsePacket.onSuccess(list);
    }

    @ApiOperation("编辑商户账号")
    @PostMapping("/edit")
    public ResponsePacket<ThirdSysUserVO> editSysThirdUser(@RequestBody SysThirdUserEditParam param) {
        TokenInfo accessToken = getAccessToken();
        param.setCreateUser(accessToken.getUserId());
        ThirdSysUserVO vo = thirdSysUserServiceImpl.editSysThirdUser(param);
        return ResponsePacket.onSuccess(vo);
    }

    @ApiOperation("启用/禁用/删除商户账号")
    @GetMapping("/status")
    public ResponsePacket updateSysThirdUserStatus(EditStatusParam param) {
        TokenInfo accessToken = getAccessToken();
        param.setCreateUser(accessToken.getUserId());
        thirdSysUserServiceImpl.updateSysThirdUserStatus(param);
        return ResponsePacket.onSuccess();
    }





}
