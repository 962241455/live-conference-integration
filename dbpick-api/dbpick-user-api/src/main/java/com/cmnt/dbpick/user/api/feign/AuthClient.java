package com.cmnt.dbpick.user.api.feign;


import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.user.UserBaseInfo;
import com.cmnt.dbpick.user.api.params.UserDefaultRegisterParam;
import com.cmnt.dbpick.user.api.params.UserRegisterParam;
import com.cmnt.dbpick.user.api.vo.UserRegisterVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "user", fallbackFactory = AuthClientFallbackFactory.class, path = "/auth")
public interface AuthClient {

    @ApiOperation(value = "直播用户服务-用户注册")
    @PostMapping(value = "/register")
    public ResponsePacket<UserRegisterVo> registerUser(
            @RequestBody UserRegisterParam registerParam);


    @ApiOperation(value = "直播用户服务-报名注册普通用户")
    @PostMapping(value = "/registerDefault")
    public ResponsePacket<UserRegisterVo> registerDefault(
            @RequestBody UserDefaultRegisterParam registerParam);
}
