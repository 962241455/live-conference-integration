package com.cmnt.dbpick.common.mongodb;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class BaseComment extends BaseDocument {

    @Indexed
    @ApiModelProperty("商户id")
    private String thirdId;

}
