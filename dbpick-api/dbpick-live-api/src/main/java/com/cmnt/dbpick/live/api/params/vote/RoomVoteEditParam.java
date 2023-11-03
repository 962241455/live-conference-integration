package com.cmnt.dbpick.live.api.params.vote;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import com.cmnt.dbpick.live.api.vo.vote.VoteInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Builder
@Data
@ApiModel(value = "直播间投票参数")
@AllArgsConstructor
@NoArgsConstructor
public class RoomVoteEditParam extends ThirdAccessKeyInfo {

    private String id;

    //@NotBlank(message = "房间号不能为空")
    @ApiModelProperty("房间号")
    private String roomNo;

    //@NotBlank(message = "投票标题不能为空")
    @ApiModelProperty("投票标题")
    private String title;

    @ApiModelProperty("是否允许放弃")
    private Boolean allowGiveUp;

    @ApiModelProperty("投票信息")
    private List<VoteInfoVO> voteInfo;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
