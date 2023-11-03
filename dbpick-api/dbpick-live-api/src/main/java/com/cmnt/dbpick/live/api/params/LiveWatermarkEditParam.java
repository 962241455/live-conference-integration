package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "直播水印参数")
public class LiveWatermarkEditParam extends ThirdAccessKeyInfo {

    private String id;

    @NotBlank(message = "水印图片URL不能为空")
    @ApiModelProperty("水印图片URL")
    private String pictureUrl;

    @NotBlank(message = "水印名称不能为空")
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
