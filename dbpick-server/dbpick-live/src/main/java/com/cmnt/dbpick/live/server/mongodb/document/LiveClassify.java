/**
 * Demo class
 *
 * @author 28021
 * @date 2022/8/11
 */
package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 房间分类
 *
 * @author
 * @date 2022/8/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "live_classify")
@TypeAlias("live_classify")
public class LiveClassify extends BaseDocument {

    /**
     * 分类名称
     */
    @Indexed
    @ApiModelProperty("分类名称")
    private String title;

    /**
     * 权重
     */
    @ApiModelProperty("权重")
    private Integer weight;
}
