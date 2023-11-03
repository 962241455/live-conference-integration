package com.cmnt.dbpick.live.api.feign;


import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author shusong.liang
 */
@FeignClient(name = "live", fallbackFactory = StreamingRoomClientFallbackFactory.class, path = "/txRtc")
public interface TRTCClient {

    @GetMapping("/userSig")
    ResponsePacket<String> getUserSig(@ApiParam(value = "用户uid集合", required = true) @RequestBody String uid);

}
