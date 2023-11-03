package com.cmnt.dbpick.live.server.mongodb.document;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description 参数
 * @date 2022-08-29 09:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rarameter {


    /**
     * @see com.cmnt.dbpick.common.enums.live.InformationTypeEnum
     */
    @ApiModelProperty("参数类型: 1、_txt  文本\n" +
            "     * 2、_name  姓名\n" +
            "     * 3、_digit  数字\n" +
            "     * 4、_select  下拉选择")
    private String informationType;

    @ApiModelProperty("参数名称")
    private String name;

    @ApiModelProperty("参数是否验证")
    private Boolean fals = Boolean.FALSE;

    @ApiModelProperty("参数值")
    private Object value;




}
