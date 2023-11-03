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
@ApiModel(value = "提交投票信息")
@AllArgsConstructor
@NoArgsConstructor
public class CommitVoteInfoParam extends ThirdAccessKeyInfo {

    //@NotBlank(message = "直播房间号 不能为空")
    @ApiModelProperty(value = "直播房间号")
    private String roomNo;

    @ApiModelProperty("投票题目id")
    private String voteId;

    @ApiModelProperty("第三方用户 ID")
    private String thirdUserId;

    @ApiModelProperty("提交投票信息")
    private List<VoteInfoVO> commitInfo;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
