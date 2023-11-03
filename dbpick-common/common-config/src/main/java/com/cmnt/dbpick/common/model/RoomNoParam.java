package com.cmnt.dbpick.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "直播房间号参数")
public class RoomNoParam extends ThirdAccessKeyInfo {

    @ApiModelProperty("房间号")
    private String roomNo;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
