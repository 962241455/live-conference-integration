package com.cmnt.dbpick.live.api.feign;

import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class LiveVoteClientFallbackFactory implements FallbackFactory<LiveVoteClient> {

    @Override
    public LiveVoteClient create(Throwable cause) {
        return new LiveVoteClient() {
            @Override
            public ResponsePacket<Boolean> stopVoteByRoomNo(String roomNo, String operator) {
                return ResponsePacket.onHystrix(cause);
            }
        };
    }
}
