package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
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
@ApiModel(value = "直播间回放视频排序设置参数")
public class RoomPlaybackSortParam extends ThirdAccessKeyInfo {

    @ApiModelProperty("回放id")
    private String id;

    /**
     * @see com.cmnt.dbpick.common.enums.SortEnum
     */
    @ApiModelProperty("排序：upward-向上； downward-向下")
    private String handleSort;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
