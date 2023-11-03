package com.cmnt.dbpick.third.server.controller;

import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.constant.Constants;
import com.cmnt.dbpick.common.entity.BizBaseExceptionEnum;
import com.cmnt.dbpick.common.entity.resp.ResponsePacket;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.jwt.JwtToken;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.utils.BeanMapUtils;
import com.cmnt.dbpick.third.api.vo.*;
import com.cmnt.dbpick.third.server.mongodb.document.ThirdUser;
import com.cmnt.dbpick.third.server.service.SysMenuService;
import com.cmnt.dbpick.third.server.service.ThirdSysUserService;
import com.cmnt.dbpick.third.server.service.ThirdUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 商户管理
 */
@Slf4j
@Api(tags = "商户管理 - 用户账号管理")
@RestController
@RequestMapping("/user")
public class ThirdUserController extends BaseController {

    @Autowired
    private ThirdUserService thirdUserServiceImpl;
    @Autowired
    private SysMenuService sysMenuServiceImpl;

    @Autowired
    private ThirdSysUserService thirdSysUserServiceImpl;

    @ApiOperation("商户用户登录接口")
    @PostMapping("/login")
    public ResponsePacket login(@RequestBody ThirdUserLoginVO vo){

        String token = null;
        try {
            ThirdUser user = thirdUserServiceImpl.createLoginToken(vo);
            //todo 保存登录日志
            thirdSysUserServiceImpl.asyncUserToRedis(user);
            TokenInfo tokenInfo = TokenInfo.builder().userId(user.getUserId()).thirdId(user.getId())
//                .userName(user.getUserName()).userAvatar(user.getUserAvatar())
                    .userRole("admin").roomNo("/")
                    .build();
            token =  JwtToken.createToken(BeanMapUtils.beanToMap(tokenInfo));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponsePacket.onError(ResponseCode.REGISTER_FAILED.getCode(),e.getMessage());
        }
        HashMap<String, Object> tokenMap = new HashMap<>(8);
        tokenMap.put(Constants.TOKEN, token);
        return ResponsePacket.onSuccess(tokenMap);
    }

    /**
     * 获取商户信息
     */
    @GetMapping("/getInfo")
    public ResponsePacket getInfo(String userName) throws Exception {
        HashMap<String, Object> infoMap = new HashMap<>(8);
        TokenInfo accessToken = getAccessToken();
        infoMap.put("user", accessToken);
        // 角色集合
        infoMap.put("roles", new HashSet<>(Arrays.asList("admin")));
        // 权限集合
        infoMap.put("permissions", new HashSet<>(Arrays.asList("*:*:*")));

        ThirdUser userInfo = thirdUserServiceImpl.findThirdUserByUserId(accessToken.getUserId());

        userInfo = Objects.isNull(userInfo)? new ThirdUser() :userInfo;
        infoMap.put("userInfo", userInfo);
        return ResponsePacket.onSuccess(infoMap);
    }

    /**
     * 获取菜单路由信息
     */
    @GetMapping("/getRouters")
    public ResponsePacket getRouters(String userName) throws Exception {
        // 用户信息
//        List<ThirdRouterVo> menus = new ArrayList<>();
//        ThirdRouterVo ak = ThirdRouterVo.builder().name("AccessKey").path("/accessKey")
//                .redirect("noRedirect").component("Layout").alwaysShow(Boolean.TRUE)
//                .meta(ThirdRouterMetaVo.builder().title("访问凭证管理").icon("logininfor").build()).build();
//        List<ThirdRouterVo> akChildren = new ArrayList<ThirdRouterVo>(){{
//            add(ThirdRouterVo.builder().name("AccessKey").path("accessKey").component("live/accessKey/index")
//                    .meta(ThirdRouterMetaVo.builder().title("AccessKey").icon("monitor").build()).build()
//            );
//        }};
//        ak.setChildren(akChildren);
//        ThirdRouterVo live = ThirdRouterVo.builder().name("live").path("/live")
//                .redirect("noRedirect").component("Layout").alwaysShow(Boolean.TRUE)
//                .meta(ThirdRouterMetaVo.builder().title("直播管理").icon("international").build()).build();
//        List<ThirdRouterVo> liveChildren = new ArrayList<ThirdRouterVo>(){{
//            add(ThirdRouterVo.builder().name("Room").path("room").component("live/room/index")
//                    .meta(ThirdRouterMetaVo.builder().title("直播间管理").icon("peoples").build()).build()
//            );
//            add(ThirdRouterVo.builder().name("Classify").path("classify").component("live/classify/index")
//                    .meta(ThirdRouterMetaVo.builder().title("分类管理").icon("peoples").build()).build()
//            );
//        }};
//        live.setChildren(liveChildren);
//        menus.add(ak);
//        menus.add(live);
        List<ThirdRouterVo> menus = sysMenuServiceImpl.getMongoMenus("third");
        return ResponsePacket.onSuccess(menus);
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public ResponsePacket logout() {
        return ResponsePacket.onSuccess();
    }




}
