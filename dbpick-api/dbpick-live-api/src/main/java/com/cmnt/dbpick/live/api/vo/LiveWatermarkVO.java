package com.cmnt.dbpick.live.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("直播间水印")
public class LiveWatermarkVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;
    private String id;

    @ApiModelProperty("关联直播房间号")
    private Set<String> usedRoomNos;

    @ApiModelProperty("水印图片URL")
    private String pictureUrl;

    @ApiModelProperty("水印ID")
    private Long watermarkId;

    @ApiModelProperty("水印名称")
    private String watermarkName;


    @ApiModelProperty("显示位置，X轴偏移，单位是百分比，默认 0")
    private Long xaxisPosition;
    @ApiModelProperty("显示位置，Y轴偏移，单位是百分比，默认 0")
    private Long yaxisPosition;
    @ApiModelProperty("水印宽度，占直播原始画面宽度百分比，建议高宽只设置一项，另外一项会自适应缩放，避免变形。默认原始宽度。")
    private Long watermarkWidth;
    @ApiModelProperty("水印高度，占直播原始画面高度百分比，建议高宽只设置一项，另外一项会自适应缩放，避免变形。默认原始高度。")
    private Long watermarkHeight;


    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

}
