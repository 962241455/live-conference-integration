package com.cmnt.dbpick.manager.server.security.service.impl;

import com.cmnt.dbpick.manager.server.entity.LoginUserDetails;
import com.cmnt.dbpick.manager.server.model.UserResp;
import com.cmnt.dbpick.manager.server.security.service.SysPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 *  *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

//    @Autowired
//    private UserFeginApi userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
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
        UserResp user = new UserResp();
        user.setUserName(username);
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(UserResp user)
    {
        return new LoginUserDetails(user, permissionService.getMenuPermission(user));
    }
}
