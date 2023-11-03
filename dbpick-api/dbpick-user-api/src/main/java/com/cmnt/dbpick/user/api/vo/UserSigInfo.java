package com.cmnt.dbpick.user.api.vo;

import com.cmnt.dbpick.common.enums.live.RoomLineTypeEnum;
import com.cmnt.dbpick.common.user.UserTokenInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户信息凭证
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSigInfo implements Serializable {

    @ApiModelProperty("用户 ID")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("房间标题")
    private String title;

    @ApiModelProperty("房间logo封面")
    private Set<String> logoCover;

    @ApiModelProperty("房间背景图")
    private String bgImg;

    @ApiModelProperty("直播间类型： meeting-多人会议； live-单人直播； record-录播; third_push-三方推流")
    private String roomType;

    @ApiModelProperty("房间状态：no_live-no_live-未开播 live_ing-直播中 forbidden-房间封禁 live_pause-直播暂停 live_over-直播结束")
    private String roomStatus;

    /**
     * @see com.cmnt.dbpick.common.enums.live.RoomLineTypeEnum
     */
    @ApiModelProperty("延迟类型: leb 快直播， cdn 标准直播")
    private String lineType;

    /**
     * @see com.cmnt.dbpick.common.enums.live.LiveWatchFilterTypeEnum
     */
    @ApiModelProperty("观看限制：no_filter-没有限制； register_filter-登记观看")
    private String watchFilter;

    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("游客观看开关限制：switch_close-关闭； switch_open-开启")
    private String visitorSwitch;

    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("回放开关：switch_close-关闭； switch_open-开启")
    private String playbackSwitch;
    @ApiModelProperty("回放过期时间(-1:永不过期)")
    private Long playbackTimeOut;

    @ApiModelProperty("开始时间")
    private String startTime;

    /**
     * @see com.cmnt.dbpick.common.enums.UserRoleEnum
     */
    @ApiModelProperty("用户角色 startv:主播 | watch:观众 | major|管理")
    private String userRole;

    @ApiModelProperty(value = "tokenInfo", hidden = true)
    private UserTokenInfoVo tokenInfoVo;

    @ApiModelProperty("userSig")
    private String userSig;
    @ApiModelProperty("shareUserSig")
    private String shareUserSig;
    @ApiModelProperty("jwtToken")
    private String jwtToken;

    @ApiModelProperty("热度值")
    private Integer hotScores;

    @ApiModelProperty("在线人数")
    private Integer online;

    public String getLineType() {
        if(StringUtils.isBlank(this.lineType)){
            return RoomLineTypeEnum.CDN.getValue();
        }
        return lineType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
