package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * 直播水印
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "live_watermark")
@TypeAlias("live_watermark")
public class LiveWatermark extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

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

    @ApiModelProperty("添加水印请求id")
    private String watermarkRequestId;


    @ApiModelProperty("水印关联直播请求id")
    private String watermarkLiveRequestId;

}
