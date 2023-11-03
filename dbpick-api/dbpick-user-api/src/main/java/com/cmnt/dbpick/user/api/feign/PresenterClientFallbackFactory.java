package com.cmnt.dbpick.user.api.feign;


import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.user.api.params.PresenterAddParam;
import com.cmnt.dbpick.user.api.vo.LiveUserVO;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class PresenterClientFallbackFactory implements FallbackFactory<PresenterClient> {

    @Override
    public PresenterClient create(Throwable cause) {
        return new PresenterClient() {
            @Override
            public ResponsePacket<LiveUserVO> addToRoom(PresenterAddParam param) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<LiveUserVO> getInfoByRoomNo(RoomNoParam param) {
                return ResponsePacket.onHystrix(cause);
            }
        };
    }
}
