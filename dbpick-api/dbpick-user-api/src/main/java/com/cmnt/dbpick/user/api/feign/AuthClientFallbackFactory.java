package com.cmnt.dbpick.user.api.feign;


import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.user.UserBaseInfo;
import com.cmnt.dbpick.user.api.params.UserDefaultRegisterParam;
import com.cmnt.dbpick.user.api.params.UserRegisterParam;
import com.cmnt.dbpick.user.api.vo.UserRegisterVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthClientFallbackFactory implements FallbackFactory<AuthClient> {

    @Override
    public AuthClient create(Throwable cause) {
        return new AuthClient() {
            @Override
            public ResponsePacket<UserRegisterVo> registerUser(UserRegisterParam registerParam) {
                return ResponsePacket.onHystrix(cause);
            }

            @Override
            public ResponsePacket<UserRegisterVo> registerDefault(UserDefaultRegisterParam registerParam) {
                return ResponsePacket.onHystrix(cause);
            }
        };
    }
}
