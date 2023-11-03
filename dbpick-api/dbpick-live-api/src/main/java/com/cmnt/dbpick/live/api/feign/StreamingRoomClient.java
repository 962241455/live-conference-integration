package com.cmnt.dbpick.live.api.feign;


import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.user.StreamingRoomTimeVO;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.live.api.vo.RoomHotOnlineVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 直播间接口
 */
@FeignClient(name = "live", fallbackFactory = StreamingRoomClientFallbackFactory.class, path = "/streaming/room")
public interface StreamingRoomClient {

   @GetMapping("/detail")
   ResponsePacket<StreamingRoomVO> detail(@RequestParam("roomNo") String roomNo);

   //查询多个房间信息
   @GetMapping("/roomNos")
   ResponsePacket<List<StreamingRoomTimeVO>> getInfoByRoomNos(@RequestParam("roomNos") String roomNos,
                                                              @RequestParam("apiPwd") String apiPwd);

   @ApiOperation("更新直播间热度和在线人数")
   @PostMapping("/hot/online")
   ResponsePacket updRoomHotAndOnline(@RequestBody RoomHotOnlineVO param);


   @ApiOperation("直播房间列表")
   @PostMapping("/list/third")
   ResponsePacket<List<String>> listByThird(@RequestBody ThirdRoomQueryParam param);


}
