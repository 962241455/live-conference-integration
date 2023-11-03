package com.cmnt.dbpick.manager.server.service;

import com.cmnt.dbpick.manager.server.entity.SysDept;
import com.cmnt.dbpick.manager.server.mongodb.document.SysMenu;

import java.util.List;

/**
 * 菜单 业务层
 *
 *  *
 */
public interface SysMenuService {

    /**
     * 构建前端路由所需要的菜单
     *
     * @return 路由列表
     */
    List<SysDept.RouterVo> getMongoMenus(String menuBelong);

}
