package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import com.cmnt.dbpick.live.api.vo.AppendixInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "直播现场参数")
public class LiveRoomSceneParam extends ThirdAccessKeyInfo {

    @ApiModelProperty(value = "id", hidden = true)
    private String id;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("信息描述")
    private String msgDesc;

    @ApiModelProperty("过期时间")
    private String overTime;

    @ApiModelProperty("附件信息")
    private List<AppendixInfoVO> appendixList;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
