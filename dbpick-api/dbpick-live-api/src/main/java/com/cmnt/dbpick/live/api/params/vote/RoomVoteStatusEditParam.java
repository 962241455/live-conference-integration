package com.cmnt.dbpick.live.api.params.vote;

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
@ApiModel(value = "直播间投票状态参数")
@AllArgsConstructor
@NoArgsConstructor
public class RoomVoteStatusEditParam extends ThirdAccessKeyInfo {

    @ApiModelProperty("投票题目id")
    private String voteId;

    /**
     * @see com.cmnt.dbpick.common.enums.live.MessageStatusEnum
     */
    @ApiModelProperty("状态：publish_ing 发布，stop_publish 结束发布，again_publish 再次发布")
    private String status;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
