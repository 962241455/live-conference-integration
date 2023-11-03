package com.cmnt.dbpick.stats.api.params;

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
@ApiModel(value = "商户vod视频回放查询参数")
public class ThirdVodPlaybackQueryParam extends ThirdAccessKeyInfo {

    @ApiModelProperty(value = "商户账号", hidden = true)
    private String thirdId;

    @ApiModelProperty("视频id")
    private String fileId;

    @ApiModelProperty("结算开始时间 yyyy-MM-dd")
    private String countStartTm;
    @ApiModelProperty("结算开始时间 yyyy-MM-dd")
    private String countEndTm;

    @ApiModelProperty("查询状态 settlement-结算期间； all-查询所有")
    private String searchStatus;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
