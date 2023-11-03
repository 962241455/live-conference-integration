package com.cmnt.dbpick.user.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "主持人/主播添Vo")
public class LiveUserVO implements Serializable {

    private static final long serialVersionUID = -5881567821062598083L;
    private String id;

    @ApiModelProperty(value = "第三方用户账号")
    private String thirdId;

    @ApiModelProperty("昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    /**
     * @see com.cmnt.dbpick.common.enums.AbleStatusEnum
     */
    @ApiModelProperty("是否可用:disable-封禁，enable-可用")
    private String enableStatus;

    /**
     * @see com.cmnt.dbpick.common.enums.LineStatusEnum
     */
    @ApiModelProperty("是否在线:offline-离线，online-在线")
    private String lineStatus;

    @ApiModelProperty("直播地址")
    private String liveAddress;

    private Long createDateTime;

}
