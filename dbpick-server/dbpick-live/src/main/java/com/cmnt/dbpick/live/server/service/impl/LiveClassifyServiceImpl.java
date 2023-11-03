/**
 * Demo class
 *
 * @author 28021
 * @date 2022/8/11
 */
package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.common.utils.JacksonUtils;
import com.cmnt.dbpick.common.utils.ObjectTools;
import com.cmnt.dbpick.live.api.params.third.GmLiveClassIfyParams;
import com.cmnt.dbpick.live.api.vo.third.LiveClassifyVo;
import com.cmnt.dbpick.live.server.mongodb.document.LiveClassify;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveClassifyRepository;
import com.cmnt.dbpick.live.server.service.LiveClassifyService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 房间分类管管理
 *
 * @author mr . wei
 * @date 2022/8/11
 */
@Service
public class LiveClassifyServiceImpl implements LiveClassifyService {

    @Resource
    MongoPageHelper mongoPageHelper;

    @Resource
    LiveClassifyRepository liveClassifyRepository;

    @Override
    public boolean save(LiveClassify liveClassify) throws Exception {

        ObjectId objectId = new ObjectId();
        liveClassify.setId(objectId.toHexString());
        liveClassify.initSave(liveClassify.getCreateUser());
        //添加类型
        LiveClassify insert = liveClassifyRepository.insert(liveClassify);
        return Boolean.TRUE;
    }

    /**
     * 通过ID获取一条信息
     *
     * @param id
     * @return
     */
    @Override
    public LiveClassify getById(String id) {
        Optional<LiveClassify> optional = liveClassifyRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BizException("该分类不存在!", ResponseCode.FAILED);
        }
        return optional.get();

    }

    @Override
    public PageResponse<LiveClassifyVo> getLiveClassifyList(GmLiveClassIfyParams params) throws Exception {
        Query query = new Query();
        if (!ObjectTools.isEmpty(params.getTitle())) {
            query.addCriteria(Criteria.where("title").regex(params.getTitle()));
        }

        PageResponse<LiveClassify> objects = mongoPageHelper.pageQuery(query, LiveClassify.class, params);
        PageResponse<LiveClassifyVo> response = JacksonUtils.toBean(JacksonUtils.toJson(objects), PageResponse.class);
        return response;
    }

    @Override
    public boolean updataLiveClassify(LiveClassify params) throws Exception {
        Optional<LiveClassify> optional = liveClassifyRepository.findById(params.getId());
        LiveClassify liveClassify = optional.get();
        FastBeanUtils.copy(params, liveClassify);
        liveClassify.setLastModifiedDate(ObjectTools.GetCurrentTime());
        liveClassify.setLastModifiedUser(liveClassify.getCreateUser());
        liveClassifyRepository.save(liveClassify);
        return Boolean.TRUE;
    }

    @Override
    public boolean isDeletel(String id) {
        LiveClassify cdNews = getById(id);
        cdNews.setDeleted(Boolean.TRUE);
        cdNews.initDel();
        cdNews.setLastModifiedDate(ObjectTools.GetCurrentTime());
        LiveClassify updateResult = liveClassifyRepository.save(cdNews);
        if (ObjectTools.isNull(updateResult)) {
            throw new BizException("删除新闻失败!", ResponseCode.FAILED);
        }
        return Boolean.TRUE;
    }

}
