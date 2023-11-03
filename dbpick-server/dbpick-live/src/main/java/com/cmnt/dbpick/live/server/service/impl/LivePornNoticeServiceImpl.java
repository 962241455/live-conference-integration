package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.utils.JacksonUtils;
import com.cmnt.dbpick.live.api.params.LivePornNoticeQueryParam;
import com.cmnt.dbpick.live.api.vo.LivePornNoticeVO;
import com.cmnt.dbpick.live.server.mongodb.document.LivePornNotice;
import com.cmnt.dbpick.live.server.service.LivePornNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


/**
 * 鉴黄通知相关接口
 */
@Slf4j
@Service
public class LivePornNoticeServiceImpl implements LivePornNoticeService {

    @Autowired
    private MongoPageHelper mongoPageHelper;

    @Override
    public PageResponse<LivePornNoticeVO> list(LivePornNoticeQueryParam param) {
        if(StringUtils.isBlank(param.getThirdId())){
            throw new BizException("房间号/商户账号 不存在!", ResponseCode.FAILED);
        }
        Query query = new Query();
        if(StringUtils.isNotBlank(param.getRoomNo())){
            query.addCriteria(Criteria.where("roomNo").is(param.getRoomNo()));
        }
        query.addCriteria(Criteria.where("thirdId").is(param.getThirdId()));
        log.info("查询鉴黄通知列表 params={}", query);
        PageResponse<LivePornNotice> list = mongoPageHelper.pageQuery(query, LivePornNotice.class, param);
        PageResponse<LivePornNoticeVO> response = JacksonUtils.toBean(JacksonUtils.toJson(list), PageResponse.class);

        return response;
    }
}
