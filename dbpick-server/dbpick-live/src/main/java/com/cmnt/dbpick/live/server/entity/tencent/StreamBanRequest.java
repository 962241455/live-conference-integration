package com.cmnt.dbpick.live.server.entity.tencent;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 * @date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreamBanRequest {

    /**
     * ForbidLiveStream
     */

    private String action;

    /**
     * 固定取值 2018-08-01
     */
    private String version;

    private String app_name;

    private String domainName;

    private String streamName;

    /**
     * UTC
     */
    private String resumeTime;

    private String reason;
}
