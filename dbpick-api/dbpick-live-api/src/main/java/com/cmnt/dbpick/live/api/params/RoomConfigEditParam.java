package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "直播间默认配置参数")
public class RoomConfigEditParam extends ThirdAccessKeyInfo {

    private String id;

    @ApiModelProperty("房间标题")
    private String title;
    @ApiModelProperty("房间logo封面")
    private String logoCover;

    @ApiModelProperty("房间背景图")
    private String bgImg;

    @ApiModelProperty("默认主播头像")
    private String streamerAvatar;

    @ApiModelProperty("房间公告")
    private String announcement;

    public String getAnnouncement() {
        if(StringUtils.isBlank(this.announcement)){
            return "欢迎收看直播....";
        }
        return announcement;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
