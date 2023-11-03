package com.cmnt.dbpick.live.api.vo;

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
@ApiModel("附件信息")
public class AppendixInfoVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("附件类型")
    private String type;

    @ApiModelProperty("链接地址")
    private String linkUrl;


}
