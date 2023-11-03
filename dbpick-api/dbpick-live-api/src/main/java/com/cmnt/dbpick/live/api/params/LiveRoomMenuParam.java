package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "视频推送到直播间参数")
public class LiveRoomMenuParam extends ThirdAccessKeyInfo {

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
