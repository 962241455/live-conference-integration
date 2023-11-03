package com.cmnt.dbpick.manager.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sys_menu")
@TypeAlias("sys_menu")
public class SysMenu extends BaseDocument {

    @ApiModelProperty("菜单ID")
    /** 菜单ID */
    private Long menuId;

    /** 菜单名称 */
    private String menuName;

    /** 父菜单名称 */
    private String parentName;

    /** 父菜单ID */
    private Long parentId;

    /** 显示顺序 */
    private String orderNum;

    /** 路由地址 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 是否为外链（0是 1否） */
    private String isFrame;

    /** 类型（M目录 C菜单 F按钮） */
    private String menuType;

    /** 所属（admin中台 third商户后台） */
    private String menuBelong;

    /** 菜单状态:0显示,1隐藏 */
    private String visible;

    /** 权限对应的URL */
    private String urls;

    /** 权限字符串 */
    private String perms;

    /** 菜单图标 */
    private String icon;

    /** 备注 */
    private String remark;

    /** 子菜单 */
    private List<SysMenu> children = new ArrayList<SysMenu>();

}
