package com.cmnt.dbpick.third.server.service;

import com.cmnt.dbpick.third.api.vo.ThirdRouterVo;

import java.util.List;

/**
 * 菜单
 */
public interface SysMenuService {

    /**
     * 构建前端路由所需要的菜单
     *
     * @return 路由列表
     */
    List<ThirdRouterVo> getMongoMenus(String menuBelong);

}
