package com.cmnt.dbpick.live.api.params;

import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.live.api.vo.watchFilter.RegisterInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "直播间登记观看限制参数")
public class RoomWatchFilterRegisterParam extends RoomNoParam {

    /**
     * @see com.cmnt.dbpick.common.enums.live.LiveWatchFilterTypeEnum
     */
    @ApiModelProperty("限制类型：no_filter-没有限制； register_filter-登记观看；")
    private String filterType;

    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("游客观看开关限制：switch_close-关闭； switch_open-开启")
    private String visitorSwitch;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("提示信息")
    private String tipInfo;

    @ApiModelProperty("登记信息")
    private List<RegisterInfoVO> registerDataList;

}
