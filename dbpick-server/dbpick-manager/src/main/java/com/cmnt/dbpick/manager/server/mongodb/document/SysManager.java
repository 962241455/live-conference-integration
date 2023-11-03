package com.cmnt.dbpick.manager.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
/**
 * 中台用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sys_manager")
@TypeAlias("sys_manager")
public class SysManager extends BaseDocument {

    @ApiModelProperty("用户账号")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String userName;

    /** 密码 */
    @ApiModelProperty("密码")
    private String password;

    /** 用户邮箱 */
    private String email;

    /** 手机号码 */
    private String phone;

    /** 用户性别 */
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 盐加密 */
    private String salt;

    /** 备注 */
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /** 帐号状态（0正常 1停用） */
    private String status;

    /** 最后登陆IP */
    private String loginIp;

    /** 最后登陆时间 */
    private Date loginDate;

}
