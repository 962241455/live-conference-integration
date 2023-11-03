package com.cmnt.dbpick.manager.server.service.impl;

import cn.hutool.core.util.StrUtil;
import com.cmnt.dbpick.manager.server.entity.SysDept.RouterVo;
import com.cmnt.dbpick.manager.server.entity.vo.MetaVo;
import com.cmnt.dbpick.manager.server.mongodb.document.SysMenu;
import com.cmnt.dbpick.manager.server.mongodb.repository.SysMenuRepository;
import com.cmnt.dbpick.manager.server.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 菜单 业务层处理
 *
 *  *
 */
@Service
public class MongoSysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    public List<SysMenu> getMenu(String menuBelong){
        List<com.cmnt.dbpick.manager.server.mongodb.document.SysMenu> menus = sysMenuRepository.findTop50ByMenuBelongAndVisibleAndDeleted(menuBelong, "0", Boolean.FALSE,
                Sort.by(Sort.Direction.ASC, "parentId").and(Sort.by(Sort.Direction.ASC, "orderNum")));
        return menus;
    }

    /**
     * 根据父节点的ID获取所有子节点
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(String menuBelong) {
        List<SysMenu> list = getMenu(menuBelong);
        Long parentId = 0L;
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<SysMenu> it = childList.iterator();
                while (it.hasNext()) {
                    SysMenu n = (SysMenu) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 构建前端路由所需要的菜单
     * @return 路由列表
     */
    @Override
    public List<RouterVo> getMongoMenus(String menuBelong) {
        List<SysMenu> menus = getChildPerms(menuBelong);
        return buildMenus(menus);
    }

    public List<RouterVo> buildMenus(List<SysMenu> menus){
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setName(StrUtil.upperFirst(menu.getPath()));
            router.setPath(getRouterPath(menu));
            router.setComponent(StrUtil.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && "M".equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            routers.add(router);
        }
        return routers;
    }


    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录
        if (0 == menu.getParentId() && "1".equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        return routerPath;
    }




}
