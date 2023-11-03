package com.cmnt.dbpick.manager.server.controller;


import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.manager.server.common.Constants;
import com.cmnt.dbpick.manager.server.entity.SysDept;
import com.cmnt.dbpick.manager.server.model.domain.AjaxResult;
import com.cmnt.dbpick.manager.server.entity.SysMenu;
import com.cmnt.dbpick.manager.server.model.LoginUser;
import com.cmnt.dbpick.manager.server.mongodb.repository.SysMenuRepository;
import com.cmnt.dbpick.manager.server.service.ISysMenuService;
import com.cmnt.dbpick.manager.server.service.SysMenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 *  *
 */
@Api(tags = "菜单控制器")
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysMenuService sysMenuServiceImpl;
    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu);
        return AjaxResult.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu dept)
    {
        List<SysMenu> menus = menuService.selectMenuList(dept);
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<SysMenu> menus = menuService.selectMenuList(new SysMenu());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * 新增菜单
     */
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu)
    {
        if (Constants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        //menu.setCreateBy(SecurityUtils.getUsername());

        int rows = menuService.insertMenu(menu);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改菜单
     */
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu)
    {
        if (Constants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        //menu.setUpdateBy(SecurityUtils.getUsername());
        int rows = menuService.updateMenu(menu);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return AjaxResult.error("菜单已分配,不允许删除");
        }

        int rows = menuService.deleteMenuById(menuId);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }


    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters(LoginUser loginUser) {
        // 用户信息
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(1L,"admin");
        List<SysDept.RouterVo> routerVos = menuService.buildMenus(menus);
        //sysMenuServiceImpl.getMongoMenus("admin")
       return AjaxResult.success(routerVos);
    }
}
