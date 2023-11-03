package com.cmnt.dbpick.stats.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import com.cmnt.dbpick.live.api.vo.vote.VoteInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 直播投票信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "live_vote")
@TypeAlias("live_vote")
public class LiveVote extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @Indexed
    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("投票标题")
    private String title;

    @ApiModelProperty("是否允许放弃")
    private Boolean allowGiveUp;

    @ApiModelProperty("投票信息")
    private List<VoteInfoVO> voteInfo;

    @ApiModelProperty("提交人数")
    private Integer commitTimes;

    /**
     * @see com.cmnt.dbpick.common.enums.live.MessageStatusEnum
     */
    @Indexed
    @ApiModelProperty("状态：no_publish 未发布，publish_ing 发布中，stop_publish 结束发布，again_publish 再次发布")
    private String status;

    /**
     *@see com.cmnt.dbpick.common.enums.live.MessageTypeEnum
     */
    @Indexed
    @ApiModelProperty("消息类型: msg_question:问卷")
    private String msgType;

    @ApiModelProperty("投票发起时间")
    private Long startTime;


}
