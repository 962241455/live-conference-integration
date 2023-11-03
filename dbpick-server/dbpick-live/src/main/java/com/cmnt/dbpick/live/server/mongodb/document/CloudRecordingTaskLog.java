package com.cmnt.dbpick.live.server.mongodb.document;

import com.cmnt.dbpick.common.entity.BaseDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 云端录制任务记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cloud_recording_task_log")
@TypeAlias("cloud_recording_task_log")
public class CloudRecordingTaskLog extends BaseDocument {

    @Indexed
    @ApiModelProperty("AccessKey ID")
    private String ak;

    @Indexed
    @ApiModelProperty("商户账号")
    private String thirdId;

    @Indexed
    @ApiModelProperty("房间号")
    private String roomNo;

    /**
     * @see com.cmnt.dbpick.common.enums.live.VideoRecordTaskStatusEnum
     */
    @ApiModelProperty("云端录制任务状态信息 Idle-空闲中; InProgress-进行中; Exited-正在退出")
    private String status;

    @Indexed
    @ApiModelProperty("腾讯返回任务id")
    private String txTaskId;

    @ApiModelProperty("腾讯返回 唯一请求 ID，定位问题时需要")
    private String txRequestId;



}
