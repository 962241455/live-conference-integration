package com.cmnt.dbpick.manager.server.security.service;

import com.cmnt.dbpick.common.enums.UserRoleEnum;
import com.cmnt.dbpick.common.jwt.JwtToken;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.utils.BeanMapUtils;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.manager.server.entity.LoginUserDetails;
import com.cmnt.dbpick.manager.server.entity.SysUser;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.manager.server.model.UserResp;
import com.cmnt.dbpick.manager.server.service.ISysUserService;
import com.cmnt.dbpick.manager.server.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 登录校验方法
 *
 *  *
 */
@Component
public class SysLoginService
{
    @Autowired
    private ISysUserService sysUserServiceImpl;

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;


    private static final String DES_KEY = "fresh-order";

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        SysUser sysUser = sysUserServiceImpl.selectUserByUserName(username);
        //todo 重写获取token，redis
        if(Objects.isNull(sysUser)){
            throw new BizException("账号不存在！");
        }
        if (!SecurityUtils.matchesPassword(password, sysUser.getPassword())) {
            throw new BizException("密码错误");
        }
        LoginUserDetails loginUser = new LoginUserDetails();
        UserResp user = new UserResp();
        FastBeanUtils.copy(sysUser,loginUser);
        FastBeanUtils.copy(sysUser,user);
        loginUser.setUser(user);

        TokenInfo tokenInfo = TokenInfo.builder().roomNo("/")
                .userId(username).userRole(UserRoleEnum.MAJOR.getValue()).build();
        String token = JwtToken.createToken(BeanMapUtils.beanToMap(tokenInfo));
        return token;

//        return tokenService.createToken(loginUser);
//        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
//        String captcha = redisCache.getCacheObject(verifyKey);
//        RedisUtils.deleteObject(verifyKey);
//        if (captcha == null)
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
//            throw new CaptchaExpireException();
//        }
//        if (!code.equalsIgnoreCase(captcha))
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
//            throw new CaptchaException();
//        }

        /*// 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUserDetails loginUser = (LoginUserDetails) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);*/
    }

//    public static void main(String[] args) {
//        System.err.println(SecurityUtils.matchesPassword("admin123", "$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2"));
//    }
}
