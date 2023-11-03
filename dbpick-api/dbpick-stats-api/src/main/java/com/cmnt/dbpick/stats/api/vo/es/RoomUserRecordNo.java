package com.cmnt.dbpick.stats.api.vo.es;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户观看记录(房间号)")
public class RoomUserRecordNo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("房间号")
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum
     */
    @ApiModelProperty("观看类型: live-直播； playback-回放")
    private String watchType;

    @ApiModelProperty("商户")
    private String thirdId;

    @ApiModelProperty("第三方用户id")
    private String thirdUserId;

    /**
     * @see com.cmnt.dbpick.common.enums.UserRoleEnum
     */
    @ApiModelProperty("用户角色 startv:主播 | watch:观众 | major|管理")
    private String userRole;

    /**
     * @see com.cmnt.dbpick.common.enums.tencent.TxUserActType
     */
    @ApiModelProperty("行为方式：enter-进入房间； exit-退出房间")
    private String actType;

    @ApiModelProperty("操作时间戳")
    private Long actTimeMillis;

    @ApiModelProperty("操作时间 yyyy-MM-dd HH:mm:ss")
    private String actTime;

    /**
     * @see com.cmnt.dbpick.common.enums.tencent.TxTerminalType
     */
    @ApiModelProperty("终端类型: 1-Windows 端; 2-Android 端; 3-iOS 端; 4-Linux 端; 100-其他")
    private String terminalType;

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("地域")
    private String area;

}
