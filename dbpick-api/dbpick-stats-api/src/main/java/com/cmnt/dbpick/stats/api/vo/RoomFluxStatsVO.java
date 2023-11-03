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
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("房间播放总流量")
public class RoomFluxStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("总流量，单位: GB。")
    private Integer sumFlux;

    @ApiModelProperty("每日总流量")
    private List<RoomFluxRecordVO> fluxRecordList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomFluxRecordVO{

        @ApiModelProperty("总流量，单位: MB。")
        private Float totalFlux;
        @ApiModelProperty("数据时间点，格式：yyyy-MM-dd")
        private String recordTime;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }

    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
