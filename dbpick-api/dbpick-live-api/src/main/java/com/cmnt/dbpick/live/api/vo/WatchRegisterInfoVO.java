package com.cmnt.dbpick.live.api.vo;

import com.cmnt.dbpick.live.api.vo.watchFilter.RegisterInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchRegisterInfoVO {

    @ApiModelProperty("房间号")
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.live.LiveWatchFilterTypeEnum
     */
    @ApiModelProperty("限制类型：no_filter-没有限制； register_filter-登记观看；")
    private String filterType;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("提示信息")
    private String tipInfo;

    @ApiModelProperty("登记信息")
    private List<RegisterInfoVO> registerDataList;


}
