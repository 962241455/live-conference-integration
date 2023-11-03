package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "直播回放查询参数")
public class RoomPlaybackQueryParam extends ThirdAccessKeyInfo {

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("播放顺序")
    private Integer playSort;

    public Integer getPlaySort() {
        if(Objects.isNull(this.playSort)){
            return 0;
        }
        return playSort;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
