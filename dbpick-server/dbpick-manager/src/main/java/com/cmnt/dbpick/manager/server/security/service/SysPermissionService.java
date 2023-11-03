package com.cmnt.dbpick.manager.server.security.service;

import com.cmnt.dbpick.manager.server.model.UserResp;
import com.cmnt.dbpick.manager.server.service.ISysMenuService;
import com.cmnt.dbpick.manager.server.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 *
 *  *
 */
@Component
public class SysPermissionService {

    @Autowired
    ISysMenuService sysMenuServiceImpl;
    @Autowired
    ISysRoleService sysRoleServiceImpl;


    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(UserResp user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (true) {
            roles.add("admin");
        } else {
            roles.addAll(sysRoleServiceImpl.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(UserResp user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
//        if (user.isAdmin()) {
        if (true) {
            roles.add("*:*:*");
        } else {
            roles.addAll(sysMenuServiceImpl.selectMenuPermsByUserId(user.getUserId()));
        }
        return roles;
    }
}
