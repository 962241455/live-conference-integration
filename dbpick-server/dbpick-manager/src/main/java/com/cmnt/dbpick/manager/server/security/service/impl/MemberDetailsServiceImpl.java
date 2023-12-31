//package com.cmnt.live.security.service.impl;
//
//import cn.hutool.core.util.ObjectUtil;
//import com.cmnt.live.entity.LoginUserDetails;
//import com.cmnt.live.security.service.SysPermissionService;
//import cn.oneplustow.api.sc.model.UserResp;
//import cn.oneplustow.api.sc.service.UserFeginApi;
//import cn.oneplustow.common.enume.UserStatus;
//import cn.oneplustow.common.exception.BaseException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
///**
// * 用户验证处理
// *
// *  *
// */
//public class MemberDetailsServiceImpl implements UserDetailsService
//{
//    private static final Logger log = LoggerFactory.getLogger(MemberDetailsServiceImpl.class);
//
//    @Autowired
//    private UserFeginApi userService;
//
//    @Autowired
//    private SysPermissionService permissionService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
//    {
//        UserResp user = userService.getUserByName(username);
//        if (ObjectUtil.isNull(user))
//        {
//            log.info("登录用户：{} 不存在.", username);
//            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
//        }
//        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
//        {
//            log.info("登录用户：{} 已被删除.", username);
//            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
//        }
//        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
//        {
//            log.info("登录用户：{} 已被停用.", username);
//            throw new BaseException("对不起，您的账号：" + username + " 已停用");
//        }
//
//        return createLoginUser(user);
//    }
//
//    public UserDetails createLoginUser(UserResp user)
//    {
//        return new LoginUserDetails(user, permissionService.getMenuPermission(user));
//    }
//}
