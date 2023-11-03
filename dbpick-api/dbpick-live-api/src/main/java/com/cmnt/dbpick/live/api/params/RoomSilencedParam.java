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
@ApiModel(value = "直播间禁言设置参数")
public class RoomSilencedParam extends RoomNoParam {

    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("禁言设置：switch_close-关闭； switch_open-开启")
    private String silencedSwitch;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
