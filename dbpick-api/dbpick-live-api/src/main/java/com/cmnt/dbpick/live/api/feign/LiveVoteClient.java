package com.cmnt.dbpick.live.api.feign;


import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 直播间接口
 */
@FeignClient(name = "live", fallbackFactory = StreamingRoomClientFallbackFactory.class, path = "/vote")
public interface LiveVoteClient {

   @ApiOperation("根据房间号停止所有投票信息")
   @GetMapping("/stop/room")
   ResponsePacket<Boolean> stopVoteByRoomNo(@RequestParam("roomNo") String roomNo,
                                            @RequestParam("operator") String operator);


}
