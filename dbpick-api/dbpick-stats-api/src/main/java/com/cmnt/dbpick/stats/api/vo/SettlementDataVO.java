package com.cmnt.dbpick.stats.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("直播结算对象")
public class SettlementDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("播放流量对象")
    private SettlementFluxVO playFluxVO;

    @ApiModelProperty("视频转码对象")
    private SettlementTransVO transVideoVO;

    @ApiModelProperty("回放流量对象")
    private SettlementPlaybackVO playbackVO;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
