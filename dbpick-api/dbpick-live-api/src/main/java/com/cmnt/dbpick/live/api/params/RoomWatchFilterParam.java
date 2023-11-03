package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.RoomNoParam;
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
@ApiModel(value = "直播间观看限制参数")
public class RoomWatchFilterParam extends RoomNoParam {

    /**
     * @see com.cmnt.dbpick.common.enums.live.LiveWatchFilterTypeEnum
     */
    @ApiModelProperty("限制类型：no_filter-没有限制； register_filter-登记观看；")
    private String filterType;

    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty(value = "游客观看开关限制：switch_close-关闭； switch_open-开启",hidden = true)
    private String visitorSwitch;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
