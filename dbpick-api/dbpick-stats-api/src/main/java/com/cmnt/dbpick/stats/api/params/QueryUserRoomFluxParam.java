package com.cmnt.dbpick.stats.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessPageInfo;
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
@ApiModel(value = "查询用户的直播间总流量参数")
public class QueryUserRoomFluxParam extends ThirdAccessPageInfo {

    @ApiModelProperty("查询开播开始时间戳")
    private String searchStartTime;
    @ApiModelProperty("查询开播结束时间戳")
    private String searchEndTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
