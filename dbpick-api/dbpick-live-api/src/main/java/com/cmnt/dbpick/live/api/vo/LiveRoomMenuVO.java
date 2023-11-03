package com.cmnt.dbpick.live.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("直播间水印")
public class LiveRoomMenuVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;
    private String id;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("菜单类型：chatMenu-聊天菜单， customizeMenu-自定义菜单")
    private String menuType;

    @ApiModelProperty("菜单标识")
    private String menuName;

    @ApiModelProperty("菜单名称")
    private String menuTitle;

    @ApiModelProperty("菜单内容")
    private String menuContent;



}
