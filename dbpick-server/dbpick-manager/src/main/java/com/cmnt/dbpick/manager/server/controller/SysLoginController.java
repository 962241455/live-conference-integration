package com.cmnt.dbpick.manager.server.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cmnt.dbpick.common.enums.UserRoleEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.jwt.JwtToken;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.utils.BeanMapUtils;
import com.cmnt.dbpick.manager.server.common.Constants;
import com.cmnt.dbpick.manager.server.model.domain.AjaxResult;
import com.cmnt.dbpick.manager.server.model.LoginBody;
import com.cmnt.dbpick.common.exception.GlobalExceptionHandler;
import com.cmnt.dbpick.manager.server.model.UserResp;
import com.cmnt.dbpick.manager.server.mongodb.document.SysManager;
import com.cmnt.dbpick.manager.server.mongodb.repository.SysManagerRepository;
import com.cmnt.dbpick.common.utils.RedisUtils;
import com.cmnt.dbpick.manager.server.util.SecurityUtils;
import com.cmnt.dbpick.manager.server.util.ServletUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.*;

/**
 * 登录验证
 *
 *  *
 */
@Slf4j
@Api(tags = "登录控制器")
@RestController
@RequestMapping("/auth")
public class SysLoginController extends GlobalExceptionHandler
{
    @Autowired
    private SysManagerRepository sysManagerRepository;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 生成验证码
     */
    @ApiOperation("生成验证码")
    @GetMapping("/captchaImage")
    public AjaxResult getCode() {
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("captchaOnOff", false);
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        Integer length = 6;
        CodeGenerator codeGenerator = new RandomGenerator(length);
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(160, 60);
        captcha.setBackground(Color.PINK);
        captcha.setFont( new Font("Arial", Font.BOLD, 48));
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        String code = captcha.getCode();
        redisUtils.set(verifyKey, code);
        ajax.put("uuid", uuid);
        ajax.put("img", captcha.getImageBase64());
        return AjaxResult.success(ajax);
    }

    /**
     * 登录方法
     * @return 结果
     */
    @ApiOperation("登录接口")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String userId = loginBody.getUsername();
//        SysManager user = sysManagerRepository.findTop1ByUserIdAndDeleted(userId,
//                Boolean.FALSE, Sort.by(Sort.Direction.DESC, "createDateTime"));
        SysManager user = sysManagerRepository.findTop1ByUserId(userId);
        //todo 重写获取token，redis
        if(Objects.isNull(user)){
            throw new BizException("账号不存在！");
        }
        if (!SecurityUtils.matchesPassword(loginBody.getPassword(), user.getPassword())) {
            throw new BizException("密码错误");
        }
        //todo 登录日志

        TokenInfo tokenInfo = TokenInfo.builder().thirdId("admin").roomNo("/")
                .userId(userId).userRole(UserRoleEnum.MAJOR.getValue()).build();
        String token = JwtToken.createToken(BeanMapUtils.beanToMap(tokenInfo));
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo(String userName)
    {
        /*LoginUser loginUser = SecurityUtils.getLoginUser();
        UserResp user = loginUser.getUser();*/
        /*// 角色集合
        Set<String> roles = permissionService.getRolePermission(new UserResp());
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(new UserResp());*/

        Set<String> roles = new HashSet<>(Arrays.asList("admin"));
        Set<String> permissions = new HashSet<>(Arrays.asList("*:*:*"));
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", new UserResp());
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        String userAvatar = "";
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            String token = request.getHeader("access_token");
            log.info("getAccessToken 获取到值为： {}", token);
            if (StrUtil.isEmpty(token)) {
                TokenInfo userInfo = BeanMapUtils.mapToBean(JwtToken.parseToken(token), TokenInfo.class);
                SysManager user = sysManagerRepository.findTop1ByUserId(userInfo.getUserId());
                userAvatar = user.getAvatar();
            }
        } catch (Exception e) {
            log.info("getAccessToken 获取到值为： {}", e.getMessage());
        }
        ajax.put("userAvatar", userAvatar);
        return ajax;
    }


    /**
     * 登出
     */
    @GetMapping("/logout")
    public AjaxResult logout() {
        return AjaxResult.success();
    }



}
