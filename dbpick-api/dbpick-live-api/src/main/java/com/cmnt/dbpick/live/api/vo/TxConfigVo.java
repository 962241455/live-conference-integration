package com.cmnt.dbpick.live.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TxConfigVo implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("推流地址")
    private String pushUrl;

    @ApiModelProperty("播放地址")
    private String playUrl;

    @ApiModelProperty("来源应用")
    private String appName;

    @ApiModelProperty("管理员用户id")
    private String adminId;

    private String masterKey;

    private String secretId;

    private String secretKey;

    private String url;

    private Long sdkappid;

    private String key;

    private String userSig;


}
