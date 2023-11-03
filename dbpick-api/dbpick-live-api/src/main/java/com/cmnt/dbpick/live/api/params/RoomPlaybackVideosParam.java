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

import java.util.List;

@Builder
@Data
@ApiModel(value = "商户端添加回放视频列表参数")
@AllArgsConstructor
@NoArgsConstructor
public class RoomPlaybackVideosParam extends ThirdAccessKeyInfo {

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("视频列表")
    private List<RoomVideoEditParam> videoList;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
