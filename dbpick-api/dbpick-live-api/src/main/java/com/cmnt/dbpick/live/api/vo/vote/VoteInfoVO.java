package com.cmnt.dbpick.live.api.vo.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("投票信息")
public class VoteInfoVO implements Serializable {

    private static final long serialVersionUID = -174928714966253420L;

    @ApiModelProperty("idx")
    private Integer idx;
    /**
     * @see com.cmnt.dbpick.common.enums.ChoiceTypeEnum
     */
    @ApiModelProperty("选择类型：radio_type-单选 checkbox_type-多选 ")
    private String choiceType;

    @ApiModelProperty("问题")
    private String question;

    @ApiModelProperty("答案选项")
    private List<VoteInfoItemVO> voteAnswers;


    public static class VoteInfoItemVO{

        @ApiModelProperty("选项id")
        private String id;
        @ApiModelProperty("answerIdx")
        private Integer answerIdx;
        @ApiModelProperty("选项内容")
        private String answerItem;
        @ApiModelProperty("选择次数")
        private Integer choiceTimes;

        public String getId() {
            if(StringUtils.isBlank(this.id)){
                return UUID.randomUUID().toString().replace("-","");
            }
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }

        public Integer getAnswerIdx() {
            return answerIdx;
        }

        public void setAnswerIdx(Integer answerIdx) {
            this.answerIdx = answerIdx;
        }

        public String getAnswerItem() {
            return answerItem;
        }
        public void setAnswerItem(String answerItem) {
            this.answerItem = answerItem;
        }

        public Integer getChoiceTimes() {
            if(StringUtils.isBlank(this.id)){
                return 0;
            }
            return choiceTimes;
        }
        public void setChoiceTimes(Integer choiceTimes) { this.choiceTimes = choiceTimes; }

        public VoteInfoItemVO() {
        }

        public VoteInfoItemVO(String id, Integer answerIdx, String answerItem) {
            this.id = id;
            this.answerIdx = answerIdx;
            this.answerItem = answerItem;
        }


        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }
    }

}
