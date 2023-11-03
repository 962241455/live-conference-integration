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


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "直播间回放设置参数")
public class RoomPlaybackNameParam extends ThirdAccessKeyInfo {

    @ApiModelProperty("回放id")
    private String id;

    @ApiModelProperty("播放视频名称")
    private String playFileName;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
