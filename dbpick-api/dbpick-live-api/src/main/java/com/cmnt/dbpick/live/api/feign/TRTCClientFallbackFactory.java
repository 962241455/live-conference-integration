package com.cmnt.dbpick.live.api.feign;

import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class TRTCClientFallbackFactory implements FallbackFactory<TRTCClient> {

    @Override
    public TRTCClient create(Throwable cause) {
        return new TRTCClient() {

            @Override
            public ResponsePacket<String> getUserSig(String uid) {
                return ResponsePacket.onHystrix(cause);
            }
        };
    }
}
