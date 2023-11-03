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
@ApiModel(value = "观众Vo")
public class LiveWatchVO implements Serializable {

    private static final long serialVersionUID = -5881567821062598083L;
    private String id;

    @ApiModelProperty(value = "第三方用户账号")
    private String thirdId;

    @ApiModelProperty("昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("观看地址")
    private String watchUrl;

    /**
     * Ip地址
     */
    private String ip;

    /**
     * 区域
     */
    private String area;


    private Long createDateTime;

}
