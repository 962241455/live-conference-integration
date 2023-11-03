package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import com.cmnt.dbpick.live.api.vo.vote.VoteInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 直播用户投票记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "live_vote_user_record")
@TypeAlias("live_vote_user_record")
public class LiveVoteUserRecord extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @Indexed
    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("投票题目id")
    private String voteId;

    @Indexed
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("第三方用户 ID")
    private String thirdUserId;

    @ApiModelProperty("提交投票信息")
    private List<VoteInfoVO> commitInfo;


}
