package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 直播间暖场
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "streaming_room_warm")
@TypeAlias("streaming_room_warm")
public class StreamingRoomWarm extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @Indexed
    @ApiModelProperty("房间号")
    private String roomNo;

    @Indexed
    @ApiModelProperty("视频id")
    private String fileId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("视频地址")
    private String videoURL;

    @ApiModelProperty("视频时长 HH:mm:ss")
    private String duration;


    @ApiModelProperty("开始类型： immediately - 立刻开始")
    private String startType;

    @ApiModelProperty("视频上传响应id")
    private String txUploadRequestId;


    @ApiModelProperty("开播时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * @see com.cmnt.dbpick.common.enums.PullStreamStatusEnum
     */
    @ApiModelProperty("任务状态: enable - 开启, pause - 关闭。")
    private String status;

    @ApiModelProperty("播放地址")
    private String playUrl;

    @Indexed
    @ApiModelProperty("任务id")
    private String txTaskId;

    @ApiModelProperty("响应id")
    private String txRequestId;



}
