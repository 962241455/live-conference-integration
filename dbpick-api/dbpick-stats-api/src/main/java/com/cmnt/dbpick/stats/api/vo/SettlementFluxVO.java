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
@ApiModel("直播结算-播放流量对象")
public class SettlementFluxVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总流量，单位: GB。")
    private Long sumFlux;

    @ApiModelProperty("播放房间列表")
    private List<DetailVO> fluxRoomList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailVO{

        @ApiModelProperty("房间号")
        private String roomNo;

        @ApiModelProperty("房间标题")
        private String title;

        @ApiModelProperty("直播间类型： meeting-多人会议； live-单人直播； record-录播; third_push-三方推流")
        private String type;

        @ApiModelProperty("播放详情")
        private List<DetailRecordVO> recordList;

        @ApiModelProperty("总流量，单位: MB。")
        private Long totalFlux;

        @ApiModelProperty("总流量，单位: GB。")
        private Long totalFluxGB;


        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DetailRecordVO{

            @ApiModelProperty("播放时间")
            private String recordTime;

            @ApiModelProperty("总流量，单位: MB。")
            private Long recordFlux;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
