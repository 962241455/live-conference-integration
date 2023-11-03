package com.cmnt.dbpick.common.model;

import io.swagger.annotations.ApiModelProperty;
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
public class SysOperLog implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("主键Id")
    private Integer id;

    @ApiModelProperty("模块标题")
    private String title;

    @ApiModelProperty("参数")
    private String optParam;

    @ApiModelProperty("业务类型（0其它 1新增 2修改 3删除）")
    private Integer businessType;

    @ApiModelProperty("路径名称")
    private String uri;

    /**
     * @see com.cmnt.dbpick.common.enums.StatusEnum
     */
    @ApiModelProperty("操作状态（1正常 0异常）")
    private Integer status;

    @ApiModelProperty("请求信息")
    private HttpServerInfo httpServerInfo;


    @ApiModelProperty("请求IP")
    private String ip;

    @ApiModelProperty("错误消息")
    private String errorMsg;

    @ApiModelProperty("操作时间")
    private Date operTime;



}
