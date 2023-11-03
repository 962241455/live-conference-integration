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
public class TxCosConfigVo implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    private String secretId;
    private String secretKey;
    private String region;

    @ApiModelProperty("存储桶")
    private String bucketName;
    @ApiModelProperty("自定义文件夹")
    private String foldersPrefix;
    @ApiModelProperty("访问域名")
    private String accessUrl;



}
