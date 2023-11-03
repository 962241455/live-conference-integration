package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.check.annotation.EnumValue;
import com.cmnt.dbpick.common.enums.DeleteFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @program: dbpick
 * @description: 消息推送
 * @author: shusong.liang
 * @create: 2022-8-12 08:36
 **/
@Validated
public class PushMessageParam implements Serializable {

    private static final long serialVersionUID = 3105933960815571763L;

    @NotNull(message = "推送形式不能为空")
    @EnumValue(enumClass = DeleteFlagEnum.class, enumField = "testp")
    private Integer testp;


    /***
     * 推送目标
     * 0：全量用户，1指定标签，2指定uid
     */
    @NotNull(message = "推送目标不能为空")
    @ApiModelProperty(value = "推送目标", required = true)
    private Integer pushTarget;

    /***
     * 用户ID字符串（半角逗号间隔）
     */
    @ApiModelProperty(value = "用户ID")
    private String uids;

    /***
     * 标签组
     */
    @ApiModelProperty(value = "标签组")
    private List<String> tagNames;

    /**
     * 目标操作系统，iOS、Android 最少传递一个
     */
    @NotEmpty(message = "目标操作系统，iOS、Android 最少传递一个")
    @ApiModelProperty(value = "目标操作系统", required = true)
    private List<Integer> platforms;
    /***
     * 发送次数：-1表示无限次
     */
    @NotNull(message = "发送次数不能为空")
    @ApiModelProperty(value = "发送次数", required = true)
    private Integer sendTimes;

    /***
     * 发送渠道（多选必填）：1、站内消息，2、站外push
     */
    @NotEmpty(message = "发送渠道不能为空")
    @ApiModelProperty(value = "发送渠道", required = true)
    private List<Integer> sendChannels;

    /***
     * 后续动作（单选必填）：
     * 打开APP首页、打开APP内部页面、打开内部H5页面，打开第三方网页
     */
    @NotNull(message = "后续操作不能为空")
    @ApiModelProperty(value = "后续操作", required = true)
    private Integer followAction;

    /***
     * 跳转地址
     * 若选择打开app内部页面、内部h5页面或第三方网页，则“跳转地址”项必填
     */
    @ApiModelProperty(value = "跳转地址")
    private String url;

    /***
     * 推送方式（单选必填）:立即推送
     */
    @NotNull(message = "推送方式不能为空")
    @ApiModelProperty(value = "推送方式", required = true)
    private Integer pushMode;

    /**
     * 推送类型：单选必填 应用消息
     */
    @NotNull(message = "推送类型不能为空")
    @ApiModelProperty(value = "推送类型", required = true)
    private Integer pushType;

    /***
     *推送形式：1：图文推送(RichContentMessage):2：纯文字推送(TextMessage)
     */
    @NotNull(message = "推送形式不能为空")
    @ApiModelProperty(value = "推送形式", required = true)
    private Integer pushForm;

    /***
     * 推送标题
     */
    @NotBlank(message = "推送标题不能为空")
    @ApiModelProperty(value = "推送标题", required = true)
    private String title;

    /***
     * 推送内容
     */
    @NotBlank(message = "推送内容不能为空")
    @ApiModelProperty(value = "推送内容", required = true)
    private String content;

    /***
     * 消息中图片地址
     */
    @ApiModelProperty(value = "消息中图片地址")
    private String imageUri;

    @NotBlank(message = "数据操作人不能为空")
    @ApiModelProperty(name = "operator", required = true)
    private String operator;

    /***
     * 消息ID
     */
    private Integer id;

    public Integer getPushTarget() {
        return pushTarget;
    }

    public void setPushTarget(Integer pushTarget) {
        this.pushTarget = pushTarget;
    }

    public String getUids() {
        return uids;
    }

    public void setUids(String uids) {
        this.uids = uids;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public List<Integer> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Integer> platforms) {
        this.platforms = platforms;
    }

    public Integer getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(Integer sendTimes) {
        this.sendTimes = sendTimes;
    }

    public List<Integer> getSendChannels() {
        return sendChannels;
    }

    public void setSendChannels(List<Integer> sendChannels) {
        this.sendChannels = sendChannels;
    }

    public Integer getFollowAction() {
        return followAction;
    }

    public void setFollowAction(Integer followAction) {
        this.followAction = followAction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPushMode() {
        return pushMode;
    }

    public void setPushMode(Integer pushMode) {
        this.pushMode = pushMode;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public Integer getPushForm() {
        return pushForm;
    }

    public void setPushForm(Integer pushForm) {
        this.pushForm = pushForm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Integer getTestp() {
        return testp;
    }

    public void setTestp(Integer testp) {
        this.testp = testp;
    }
}

