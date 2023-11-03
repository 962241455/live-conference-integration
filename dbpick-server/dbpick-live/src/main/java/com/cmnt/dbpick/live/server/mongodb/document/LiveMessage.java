package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 直播消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "live_message")
@TypeAlias("live_message")
public class LiveMessage extends BaseDocument {

    @Indexed
    @ApiModelProperty("房间号")
    private String roomNo;

    @Indexed
    @ApiModelProperty("消息发起人id")
    private String initiatorUserId;

    @ApiModelProperty("发起人昵称")
    private String initiatorUserName;

    @ApiModelProperty("发起人头像")
    private String initiatorUserAvatar;


    /**
     *@see com.cmnt.dbpick.common.enums.live.MessageTypeEnum
     */
    @Indexed
    @ApiModelProperty("消息类型：msg_announcement:公告 ; msg_sign:签到 ; " +
            "msg_exam:考试 ; msg_question:问卷 ; msg_popup:弹窗")
    private String msgType;

    @ApiModelProperty("消息内容")
    private String msgInfo;

    @ApiModelProperty("发起时间")
    private Long initiateTime;
}
