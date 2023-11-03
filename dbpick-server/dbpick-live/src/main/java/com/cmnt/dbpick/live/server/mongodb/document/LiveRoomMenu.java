package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 直播间菜单配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "live_room_menu")
@TypeAlias("live_room_menu")
public class LiveRoomMenu extends BaseDocument {

    @Indexed
    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("菜单类型：chatMenu-聊天菜单， customizeMenu-自定义菜单")
    private String menuType;

    @Indexed
    @ApiModelProperty("菜单标识")
    private String menuName;

    @ApiModelProperty("菜单名称")
    private String menuTitle;

    @ApiModelProperty("菜单内容")
    private String menuContent;

}
