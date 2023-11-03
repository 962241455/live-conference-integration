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
@ApiModel(value = "主持人/主播编辑参数")
public class LiveUserEditParam extends ThirdAccessKeyInfo {

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id", required = true)
    private String id;

    @NotNull(message = "昵称不能为空")
    @ApiModelProperty(value = "昵称", required = true)
    private String userName;

    @NotNull(message = "用户头像不能为空")
    @ApiModelProperty("用户头像")
    private String userAvatar;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
