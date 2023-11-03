package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.LivePornNoticeQueryParam;
import com.cmnt.dbpick.live.api.vo.LivePornNoticeVO;



/**
 * 鉴黄通知相关接口
 */
public interface LivePornNoticeService {

    /**
     * 查询投票信息
     */
    PageResponse<LivePornNoticeVO> list(LivePornNoticeQueryParam param);

}
