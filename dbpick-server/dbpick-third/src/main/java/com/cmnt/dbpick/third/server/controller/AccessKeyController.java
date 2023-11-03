package com.cmnt.dbpick.third.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.enums.AbleStatusEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.user.ThirdAccessKeyVO;
import com.cmnt.dbpick.third.api.params.EditStatusParam;
import com.cmnt.dbpick.third.server.service.ThirdAccessKeyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 访问凭证相关接口
 */
@Slf4j
@Api(tags = {"访问凭证相关接口"})
@RequestMapping("/accessKey")
@RestController
public class AccessKeyController extends BaseController {

    @Autowired
    private ThirdAccessKeyService thirdAccessKeyServiceImpl;

    @ApiOperation("获取访问凭证")
    @GetMapping("/info")
    public ResponsePacket<ThirdAccessKeyVO> info() {
        TokenInfo accessToken = getAccessToken();
        ThirdAccessKeyVO vo = thirdAccessKeyServiceImpl.findAccessKeyByUser(accessToken.getThirdId());
        return ResponsePacket.onSuccess(vo);
    }


    @ApiOperation("查看Secret")
    @GetMapping("/secretInfo")
    public ResponsePacket<String> secretInfo() {
        //todo 发送短信，缓存在 redis
        TokenInfo accessToken = getAccessToken();
        ThirdAccessKeyVO vo = thirdAccessKeyServiceImpl.findAccessKeyByUser(accessToken.getThirdId());
        if(Objects.isNull(vo)){
            throw new BizException(ResponseCode.NOT_FIND_ACCESS);
        }
        if(Objects.nonNull(vo) && StringUtils.equals(vo.getStatus(), AbleStatusEnum.DISABLE.getValue())){
            throw new BizException(ResponseCode.ACCESS_KEY_DISABLE);
        }
        return ResponsePacket.onSuccess(vo.getAccessKeySecret());
    }


    @ApiOperation("生成访问凭证")
    @GetMapping("/createAccess")
    public ResponsePacket<ThirdAccessKeyVO> createAccess() {
        TokenInfo accessToken = getAccessToken();
        ThirdAccessKeyVO vo = thirdAccessKeyServiceImpl.createAccess(accessToken.getThirdId(),accessToken.getUserId());
        return ResponsePacket.onSuccess(vo);
    }


    @ApiOperation("更新状态")
    @GetMapping("/updateAccess")
    public ResponsePacket<ThirdAccessKeyVO> updateAccess(EditStatusParam param) {
        TokenInfo accessToken = getAccessToken();
        param.setCreateUser(accessToken.getUserId());
        ThirdAccessKeyVO accessVo = thirdAccessKeyServiceImpl.updateAccess(param);
        return ResponsePacket.onSuccess(accessVo);
    }


    @ApiOperation("校验ak sk 是否可用")
    @GetMapping("/existsAccess")
    public ResponsePacket<ThirdAccessKeyVO> existsAccess(@RequestParam("accessKeyId") String accessKeyId,
                                       @RequestParam("accessKeySecret")String accessKeySecret) {
        ThirdAccessKeyVO vo = thirdAccessKeyServiceImpl.existsAccess(accessKeyId, accessKeySecret);
        return ResponsePacket.onSuccess(vo);
    }


    @ApiOperation("根据创建人获取访问凭证")
    @GetMapping("/infoByCreateUser")
    public ResponsePacket<ThirdAccessKeyVO> findAccessByCreateUser(
            @RequestParam("createUser") String createUser) {
        ThirdAccessKeyVO vo = thirdAccessKeyServiceImpl.findAccessKeyByUser(createUser);
        if(Objects.isNull(vo)){
            throw new BizException(ResponseCode.NOT_FIND_ACCESS);
        }
        return ResponsePacket.onSuccess(vo);
    }


}
