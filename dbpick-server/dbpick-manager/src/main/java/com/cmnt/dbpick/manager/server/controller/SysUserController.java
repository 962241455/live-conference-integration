package com.cmnt.dbpick.manager.server.controller;


import cn.hutool.core.util.ObjectUtil;
import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.manager.server.common.Constants;
import com.cmnt.dbpick.manager.server.entity.SysUser;

import com.cmnt.dbpick.manager.server.entity.criteria.SysUserListCriteria;
import com.cmnt.dbpick.manager.server.model.SimpleUser;
import com.cmnt.dbpick.manager.server.model.domain.AjaxResult;
import com.cmnt.dbpick.manager.server.model.domain.TableDataInfo;
import com.cmnt.dbpick.manager.server.service.ISysPostService;
import com.cmnt.dbpick.manager.server.service.ISysRoleService;
import com.cmnt.dbpick.manager.server.service.ISysUserService;
import com.cmnt.dbpick.manager.server.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户信息
 *
 *  *
 */
@Api(tags = "用户信息控制器")
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public AjaxResult list(SysUserListCriteria criteria) {
        TableDataInfo<SysUser> data = userService.selectUserList(criteria);
        return AjaxResult.success(data);
    }

    /**
     * 根据角色获取用户
     */
    @GetMapping("/listByRole")
    public AjaxResult listByRole(String roleKey) {
        List<SimpleUser> list = userService.selectUserListByRole(roleKey);
        return AjaxResult.success(list);
    }

    @ApiOperation("导出用户列表")
    @PostMapping("/export")
    public void export(SysUserListCriteria criteria, HttpServletResponse response) {
    }


    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("roles", roleService.selectRoleAll());
        ajax.put("posts", postService.selectPostAll());
        if (ObjectUtil.isNotNull(userId)) {
            ajax.put("user", userService.selectUserById(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        //user.setCreateBy(SecurityUtils.getUsername());
        int rows = userService.insertUser(user);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改用户
     */
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (Constants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (Constants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        //user.setUpdateBy(SecurityUtils.getUsername());
        int rows = userService.updateUser(user);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        int rows = userService.deleteUserByIds(userIds);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 重置密码
     */
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        //user.setUpdateBy(SecurityUtils.getUsername());

        int rows = userService.resetPwd(user);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        //user.setUpdateBy(SecurityUtils.getUsername());
        int rows = userService.updateUserStatus(user);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
