package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 直播签到记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "live_sign_record")
@TypeAlias("live_sign_record")
public class LiveSignRecord extends BaseDocument {

    @Indexed
    @ApiModelProperty("房间号")
    private String roomNo;

    @Indexed
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;
    

    @ApiModelProperty("签到时间")
    private Long signTime;



}
