package com.cmnt.dbpick.live.server.mongodb.document;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description 登记观看
 * @date 2022-08-27 16:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisInfo {

    @ApiModelProperty("欢迎标题")
    private  String title;

    @ApiModelProperty("提示信息")
    private String content;

    @ApiModelProperty("参数")
    private List<Rarameter> rarameters;

    /**
     * @see com.cmnt.dbpick.common.enums.live.InformationTypeEnum
     */
    @ApiModelProperty("直播间类型： _txt - 文本 | _name - 姓名  |  _digit - 数字  | _select -  下拉选择")
    private String InformationType;
}
