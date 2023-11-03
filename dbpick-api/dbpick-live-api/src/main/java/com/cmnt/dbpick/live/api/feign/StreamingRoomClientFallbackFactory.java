package com.cmnt.dbpick.live.api.feign;

import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.user.StreamingRoomTimeVO;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.live.api.params.ThirdRoomQueryParam;
import com.cmnt.dbpick.live.api.vo.RoomHotOnlineVO;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StreamingRoomClientFallbackFactory implements FallbackFactory<StreamingRoomClient> {

    @Override
    public StreamingRoomClient create(Throwable cause) {
        return new StreamingRoomClient() {

            @Override
            public ResponsePacket<StreamingRoomVO> detail(String roomNo) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<List<StreamingRoomTimeVO>> getInfoByRoomNos(String roomNos, String apiPwd) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket updRoomHotAndOnline(RoomHotOnlineVO param) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<List<String>> listByThird(ThirdRoomQueryParam param) {
                return ResponsePacket.onHystrix(cause);
            }


        };
    }
}
