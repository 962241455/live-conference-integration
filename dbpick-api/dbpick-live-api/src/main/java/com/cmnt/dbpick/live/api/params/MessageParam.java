package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.enums.live.MessageTypeEnum;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.mq.constant.MqDelayLevelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "消息参数")
public class MessageParam extends RoomNoParam {

    /**
     *@see com.cmnt.dbpick.common.enums.live.MessageTypeEnum
     */
    @NotBlank
    @ApiModelProperty("消息类型：msg_announcement:公告 ; msg_sign:签到 ; " +
            "msg_exam:考试 ; msg_question:问卷 ; msg_popup:弹窗")
    private String msgType;

    @ApiModelProperty("消息内容")
    private String msgInfo;

    @ApiModelProperty(value = "发起人id", hidden = true)
    private String initiatorUserId;

    /**
     * @see com.cmnt.dbpick.common.mq.constant.MqDelayLevelEnum
     */
    @ApiModelProperty("延迟签到时间(默认立刻开始)：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h")
    private String delayLevel;


    public String getSignMsg() {
        if(StringUtils.isBlank(this.msgInfo)){
            return MessageTypeEnum.getByValue(this.msgType).getDesc();
        }
        return msgInfo;
    }


    public String getDelayLevel() {
        if(StringUtils.isBlank(this.delayLevel)){
            return MqDelayLevelEnum.NOW.getDescription();
        }
        return delayLevel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
