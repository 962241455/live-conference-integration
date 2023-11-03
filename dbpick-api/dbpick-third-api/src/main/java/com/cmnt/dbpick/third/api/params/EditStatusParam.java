package com.cmnt.dbpick.third.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "更改状态参数")
public class EditStatusParam implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @NotBlank(message = "数据id不能为空")
    @ApiModelProperty("数据id")
    private String id;

    /**
     * @see com.cmnt.dbpick.common.enums.AbleStatusEnum
     */
    @NotBlank(message = "操作类型不能为空")
    @ApiModelProperty("操作类型: disable-禁用,enable-启动, delete-删除")
    private String type;

    @ApiModelProperty("操作员")
    private String createUser;

}
