package com.cmnt.dbpick.user.api.feign;


import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.user.UserBaseInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "user", fallbackFactory = UserClientFallbackFactory.class, path = "/user")
public interface UserClient {

    @ApiOperation("根据id查询用户信息")
    @GetMapping("/find/info")
    ResponsePacket<UserBaseInfo> findUserBaseInfo(@RequestParam("uid") String uid);


}
