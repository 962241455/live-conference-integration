package com.cmnt.dbpick.user.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchFilterRegisterInfoVO {

    @ApiModelProperty("信息类型 registerName-姓名，registerCompany-单位，registerProfession-职称," +
            " registerPhone-电话, registerArea-地区")
    private String infoType;

    @ApiModelProperty("信息标题")
    private String infoTitle;

    @ApiModelProperty("信息描述")
    private String infoDesc;

    @ApiModelProperty("信息值")
    private String infoValue;


}
