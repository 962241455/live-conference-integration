package com.cmnt.dbpick.stats.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;


/**
 * im事件回调
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "im回调用户行为记录参数")
public class RoomUserRecordParam implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("回调命令字")
    private String CallbackCommand;

    @ApiModelProperty("回调响应id")
    private String RequestID;

    @ApiModelProperty("客户端平台，对应不同的平台类型，可能的取值有：\n" +
            "         * RESTAPI（使用 REST API 发送请求）、Web（使用 Web SDK 发送请求）、\n" +
            "         * Android、iOS、Windows、Mac、iPad、Unknown（使用未知类型的设备发送请求）")
    private String OptPlatform;

    @ApiModelProperty("客户端 IP")
    private String ClientIP;

    @ApiModelProperty("应用标识")
    private String SdkAppid;

    @ApiModelProperty("群组 ID（房间号）")
    private String GroupId;

    @ApiModelProperty("群组类型介绍，例如 Public")
    private String Type;

    @ApiModelProperty("操作者")
    private String Operator_Account;

    @ApiModelProperty("操作时间戳(毫秒")
    private Long EventTime;

    /**
     * @see com.cmnt.dbpick.common.enums.tencent.TxUserActType
     */
    @ApiModelProperty("行为方式：enter-进入房间； exit-退出房间")
    private String actType;

    @ApiModelProperty("入群方式：Apply（申请入群）；Invited（邀请入群）/ 离开方式：Kicked 为被群主移出群聊；Quit 为主动退群")
    private String actReason;

    @ApiModelProperty("成员 UserID 集合")
    private List<String> memberIdList;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
