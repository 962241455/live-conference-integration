package com.cmnt.dbpick.live.api.vo;

import io.swagger.annotations.ApiModel;
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
@ApiModel("直播鉴黄通知")
public class LivePornNoticeVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;
    private String id;

    @ApiModelProperty("商户账号")
    private String thirdId;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("图片链接")
    private String screenShotImg;

    @ApiModelProperty("图片的 OCR 识别信息")
    private String ocrMsg;

    /**
     *@see com.cmnt.dbpick.common.enums.tencent.PornNoticeSuggestionEnum
     */
    @ApiModelProperty("建议处理方式： Block：打击； Review：待复审; Pass：正常")
    private String suggestion;

    @ApiModelProperty("检测结果中优先级最高的恶意标签对应的分类值")
    private String type;
    @ApiModelProperty("type 对应的评分")
    private String score;
    @ApiModelProperty("检测结果中所对应的优先级最高的恶意标签，表示模型推荐的审核结果，对不同违规类型与建议值进行处理")
    private String label;
    @ApiModelProperty("返回检测结果所命中优先级最高的恶意标签下的子标签名称")
    private String subLabel;

    @ApiModelProperty("截图时间")
    private Long screenShotTime;

    @ApiModelProperty("请求发送时间，UNIX 时间戳")
    private Long sendTime;

    @ApiModelProperty("推流域名")
    private String txApp;
    @ApiModelProperty("业务ID")
    private Long txAppId;
    @ApiModelProperty("推流名称")
    private String txAppName;
    @ApiModelProperty("推流参数")
    private String txStreamParam;


    @ApiModelProperty("创建时间戳")
    private Long createDateTime;

}
