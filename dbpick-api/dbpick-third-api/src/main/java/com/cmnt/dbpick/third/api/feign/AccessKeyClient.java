package com.cmnt.dbpick.third.api.feign;


import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.user.ThirdAccessKeyVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 直播间接口
 */
@FeignClient(name = "third", fallbackFactory = AccessKeyClientFallbackFactory.class, path = "/accessKey")
public interface AccessKeyClient {

   @ApiOperation("校验ak sk 是否可用")
   @GetMapping("/existsAccess")
   ResponsePacket<ThirdAccessKeyVO> existsAccess(@RequestParam("accessKeyId") String accessKeyId,
                               @RequestParam("accessKeySecret")String accessKeySecret);

   @ApiOperation("根据创建人获取访问凭证")
   @GetMapping("/infoByCreateUser")
   ResponsePacket<ThirdAccessKeyVO> findAccessByCreateUser(@RequestParam("createUser") String createUser);

}
