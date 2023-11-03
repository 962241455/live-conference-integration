//package com.cmnt.dbpick.third.api.feign;
//
//import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
//import com.cmnt.dbpick.common.user.ThirdAccessKeyVO;
//import feign.hystrix.FallbackFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AccessKeyClientFallbackFactory implements FallbackFactory<AccessKeyClient> {
//
//    @Override
//    public AccessKeyClient create(Throwable cause) {
//        return new AccessKeyClient() {
//            @Override
//            public ResponsePacket existsAccess(String accessKeyId, String accessKeySecret) {
//                return ResponsePacket.onHystrix(cause);
//            }
//
//            @Override
//            public ResponsePacket<ThirdAccessKeyVO> findAccessByCreateUser(String createUser) {
//                return ResponsePacket.onHystrix(cause);
//            }
//        };
//    }
//}
