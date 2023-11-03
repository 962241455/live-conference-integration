package com.cmnt.dbpick.user.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

/**
 * @author shusong.liang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "主播添加参数")
public class StreamerAddParam extends ThirdAccessKeyInfo {

    @NotNull(message = "直播房间号不能为空")
    @ApiModelProperty(value = "直播房间号", required = true)
    private String roomNo;

    @ApiModelProperty(value = "直播标题", required = true)
    private String title;

    @ApiModelProperty(value = "昵称", required = true)
    private String userName;

    @ApiModelProperty(value = "角色权限")
    private String userRole;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
