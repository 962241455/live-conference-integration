package com.cmnt.dbpick.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description 描述
 * @date 2023-03-27 21:39
 */
@Data
@Builder
public class HttpServerInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("端口")
    private Integer port;

    @ApiModelProperty("服务器")
    private String name;

    @ApiModelProperty("项目名")
    private String contextPath;

    @ApiModelProperty("ServletPath")
    private String servletPath;

    @ApiModelProperty("RequestURI")
    private String uri;
}
