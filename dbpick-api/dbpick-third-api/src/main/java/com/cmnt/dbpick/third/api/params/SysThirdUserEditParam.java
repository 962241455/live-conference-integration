package com.cmnt.dbpick.third.api.params;

import com.cmnt.dbpick.common.constant.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "商户账号添加/编辑参数")
public class SysThirdUserEditParam implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    private String id;

    @NotBlank(message = "商户账号不能为空")
    @ApiModelProperty("商户账号")
    private String thirdId;

    @ApiModelProperty("商户名称")
    private String thirdName;

    @ApiModelProperty("商户套餐id")
    private String thirdMerchantPackageId;

    @ApiModelProperty("是否无限制")
    private Boolean limit;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty("头像")
    private String thirdAvatar;

    @ApiModelProperty("创建人")
    private String createUser;

    public String getThirdName() {
        if(StringUtils.isBlank(this.thirdName)){
            return this.thirdId;
        }
        return this.thirdName;
    }

    public Boolean getLimit() {
        if(Objects.isNull(this.limit)){
            return Boolean.FALSE;
        }
        return this.limit;
    }

    public String getThirdAvatar() {
        if(StringUtils.isBlank(this.thirdAvatar)){
            return Constants.getDefaultImgae();
        }
        return thirdAvatar;
    }
}
