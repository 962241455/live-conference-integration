package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
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
@ApiModel(value = "商户直播间查询参数")
public class ThirdRoomQueryParam extends ThirdAccessKeyInfo {

    @ApiModelProperty(value = "商户账号", hidden = true)
    private String thirdId;

    @ApiModelProperty("查询开始时间： yyyy-MM-dd")
    private String searchStartTime;
    @ApiModelProperty("查询结束时间： yyyy-MM-dd")
    private String searchEndTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
