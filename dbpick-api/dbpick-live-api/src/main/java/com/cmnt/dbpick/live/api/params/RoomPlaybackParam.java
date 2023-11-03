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
@ApiModel(value = "直播间回放设置参数")
public class RoomPlaybackParam extends RoomNoParam {

    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("回放开关：switch_close-关闭； switch_open-开启")
    private String playbackSwitch;

    @ApiModelProperty("回放过期时间(-1:永不过期)")
    private Long playbackTimeOut;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
