package com.cmnt.dbpick.user.api.params;

import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import com.cmnt.dbpick.user.api.vo.WatchFilterRegisterInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 登记观看信息参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "登记观看信息参数")
public class WatchFilterRegisterParam extends ThirdAccessKeyInfo {


    @NotBlank(message = "直播房间号 不能为空")
    @ApiModelProperty(value = "直播房间号")
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.live.LiveWatchFilterTypeEnum
     */
    @ApiModelProperty("是否登记：no_filter-没有； register_filter-登记")
    private String watchFilter;

    /**
     * @see com.cmnt.dbpick.common.enums.SwitchEnum
     */
    @ApiModelProperty("是否为游客：switch_close-不是； switch_open-是")
    private String visitorSwitch;

    @ApiModelProperty("第三方用户 ID")
    private String thirdUserId;

    @ApiModelProperty("登记信息")
    private List<WatchFilterRegisterInfoVO> registerDataList;




}
