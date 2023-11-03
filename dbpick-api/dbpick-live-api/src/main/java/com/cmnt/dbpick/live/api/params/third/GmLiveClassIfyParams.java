package com.cmnt.dbpick.live.api.params.third;


import com.cmnt.dbpick.common.page.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CdNftActivityQueryParams
 *
 * @author shusong.liang
 * @date 2022/4/13 23:01
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class GmLiveClassIfyParams extends PageRequest {

    @ApiModelProperty(value = "标题", required = false)
    private String title;

//    @ApiModelProperty(value = "排序", required = false)
//    private Integer weight;
}
