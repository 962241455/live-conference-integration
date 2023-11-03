package com.cmnt.dbpick.stats.api.vo;

import com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户观看直播记录")
public class LiveUserRecordVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("房间号")
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum
     */
    @ApiModelProperty("观看类型: live-直播； playback-回放")
    private String watchType;

    @ApiModelProperty("用户角色：主播、观众")
    private String userRole;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    /**
     * @see com.cmnt.dbpick.common.enums.tencent.TxUserActType
     */
    @ApiModelProperty("行为方式：enter-进入房间； exit-退出房间")
    private String actType;

    @ApiModelProperty("操作时间")
    private String actTime;

    /**
     * @see com.cmnt.dbpick.common.enums.tencent.TxTerminalType
     */
    @ApiModelProperty("终端类型: 1-Windows 端; 2-Android 端; 3-iOS 端; 4-Linux 端; 100-其他")
    private String terminalType;


    @ApiModelProperty("地域")
    private String ip;

    @ApiModelProperty("地域")
    private String area;

    private String reason;

    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

    public String getWatchType() {
        if(StringUtils.isBlank(this.watchType)){
            return WatchRoomTypeEnum.LIVE.getValue();
        }
        return watchType;
    }
}
