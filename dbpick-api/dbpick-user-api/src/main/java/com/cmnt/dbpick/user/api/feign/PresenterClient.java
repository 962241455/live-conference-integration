package com.cmnt.dbpick.user.api.feign;


import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.user.api.params.PresenterAddParam;
import com.cmnt.dbpick.user.api.vo.LiveUserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "user", fallbackFactory = PresenterClientFallbackFactory.class, path = "/presenter")
public interface PresenterClient {

    @ApiOperation("给直播间添加主持人")
    @PostMapping("/addToRoom")
    ResponsePacket<LiveUserVO> addToRoom(@RequestBody PresenterAddParam param);


    @ApiOperation("根据房间号查询主持人")
    @PostMapping("/info")
    ResponsePacket<LiveUserVO> getInfoByRoomNo(@RequestBody RoomNoParam param);
}
