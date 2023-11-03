package com.cmnt.dbpick.live.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("直播现场")
public class LiveRoomSceneVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;
    private String id;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("信息描述")
    private String msgDesc;

    @ApiModelProperty("过期时间")
    private String overTime;


    @ApiModelProperty("附件信息")
    private Set<AppendixInfoVO> appendixList;


    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("现场开关：switch_close-下架； switch_open-上架")
    private String statusSwitch;


    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

}
