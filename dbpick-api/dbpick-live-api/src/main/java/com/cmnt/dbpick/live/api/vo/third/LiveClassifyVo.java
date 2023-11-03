package com.cmnt.dbpick.live.api.vo.third;

import com.cmnt.dbpick.common.entity.BaseDocumentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @TableName account
 * 新闻实体
 */
@Data
public class LiveClassifyVo extends BaseDocumentVO {

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("排序")
    private Integer weight;

}
