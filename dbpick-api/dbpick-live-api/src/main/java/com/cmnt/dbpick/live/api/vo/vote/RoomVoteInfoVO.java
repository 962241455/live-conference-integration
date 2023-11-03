package com.cmnt.dbpick.live.api.vo.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("直播投票信息")
public class RoomVoteInfoVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;
    private String id;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("投票标题")
    private String title;

    @ApiModelProperty("投票信息")
    private List<VoteInfoVO> voteInfo;


    @ApiModelProperty("提交人数")
    private Integer commitTimes;

    /**
     * @see com.cmnt.dbpick.common.enums.live.MessageStatusEnum
     */
    @ApiModelProperty("状态：no_publish 未发布，publish_ing 发布中，stop_publish 结束发布，again_publish 再次发布")
    private String status;


    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

}
