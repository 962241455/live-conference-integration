package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.tx.tencent.TxCloudLiveUtil;
import com.cmnt.dbpick.common.tx.tencent.request.live.TxLiveWatermarkParam;
import com.cmnt.dbpick.common.tx.tencent.response.live.TxLiveWatermarkResponse;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.common.utils.JacksonUtils;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.LiveWatermarkVO;
import com.cmnt.dbpick.live.server.mongodb.document.LiveWatermark;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveWatermarkRepository;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import com.cmnt.dbpick.live.server.service.WatermarkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 水印管理
 */
@Slf4j
@Service
public class WatermarkServiceImpl implements WatermarkService {


    @Autowired
    private TxCloudLiveUtil txCloudLiveUtil;

    @Autowired
    private MongoPageHelper mongoPageHelper;

    @Autowired
    private LiveWatermarkRepository liveWatermarkRepository;
    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;



    /**
     * 查询水印图片列表
     */
    @Override
    public PageResponse<LiveWatermarkVO> getList(LiveWatermarkQueryParams param){
        Query query = new Query();
        if (Objects.nonNull(param.getWatermarkId())) {
            query.addCriteria(Criteria.where("watermarkId").is(param.getWatermarkId()));
        }
        if (StringUtils.isNotBlank(param.getWatermarkName())) {
            query.addCriteria(Criteria.where("watermarkName").regex(param.getWatermarkName()));
        }
        query.addCriteria(Criteria.where("thirdId").is(param.getThirdId()));
        log.info("查询水印图片列表 getList params={}", query);
        PageResponse<LiveWatermark> objects = mongoPageHelper.pageQuery(query, LiveWatermark.class, param);
        PageResponse<LiveWatermarkVO> response = JacksonUtils.toBean(JacksonUtils.toJson(objects), PageResponse.class);
        return response;
    }


    /**
     * 直播间上传到Vod视频路径-保存
     */
    @Override
    public Boolean add(LiveWatermarkEditParam param){
        String ak = streamingRoomServiceImpl.checkAccessKeyByThirdId(param.getThirdId());
        LiveWatermark data = new LiveWatermark();
        FastBeanUtils.copy(param, data);
        data.setAk(ak);
        data.setThirdId(param.getThirdId());

        TxLiveWatermarkParam txParam = new TxLiveWatermarkParam();
        FastBeanUtils.copy(param, txParam);
        txParam.setWidth(param.getWatermarkWidth());
        txParam.setHeight(param.getWatermarkHeight());
        txParam.setXPosition(param.getXaxisPosition());
        txParam.setYPosition(param.getYaxisPosition());
        TxLiveWatermarkResponse result = txCloudLiveUtil.addWatermark(txParam);
        data.setWatermarkId(result.getWatermarkId());
        data.setWatermarkRequestId(result.getRequestId());

        data.initSave(param.getCreateUser());
        liveWatermarkRepository.save(data);
        return Boolean.TRUE;
    }


    /**
     * 更新水印图片
     */
    @Override
    public LiveWatermarkVO update(LiveWatermarkEditParam param) {
        Optional<LiveWatermark> optional = liveWatermarkRepository.findById(param.getId());
        if (!optional.isPresent()) {
            throw new BizException(ResponseCode.WATERMARK_NOT_EXIST.getMsg());
        }
        LiveWatermark watermark = optional.get();
        if(StringUtils.isNotBlank(param.getPictureUrl())){
            watermark.setPictureUrl(param.getPictureUrl());
        }
        if(StringUtils.isNotBlank(param.getWatermarkName())){
            watermark.setWatermarkName(param.getWatermarkName());
        }
        if(Objects.nonNull(param.getWatermarkWidth())){
            watermark.setWatermarkWidth(param.getWatermarkWidth());
        }
        if(Objects.nonNull(param.getWatermarkHeight())){
            watermark.setWatermarkHeight(param.getWatermarkHeight());
        }
        if(Objects.nonNull(param.getXaxisPosition())){
            watermark.setXaxisPosition(param.getXaxisPosition());
        }
        if(Objects.nonNull(param.getYaxisPosition())){
            watermark.setYaxisPosition(param.getYaxisPosition());
        }

        TxLiveWatermarkParam txParam = new TxLiveWatermarkParam();
        FastBeanUtils.copy(watermark, txParam);
        txParam.setWidth(param.getWatermarkWidth());
        txParam.setHeight(param.getWatermarkHeight());
        txParam.setXPosition(param.getXaxisPosition());
        txParam.setYPosition(param.getYaxisPosition());
        TxLiveWatermarkResponse result = txCloudLiveUtil.updateWatermark(txParam);
        watermark.setWatermarkRequestId(result.getRequestId());

        watermark.initUpdate(param.getCreateUser());
        liveWatermarkRepository.save(watermark);
        LiveWatermarkVO vo = new LiveWatermarkVO();
        FastBeanUtils.copy(watermark, vo);
        return vo;
    }

    /**
     * 删除水印
     * @param id 水印id
     * @param createUser 操作员
     * @return
     */
    @Override
    public Boolean deleteWatermark(String id, String createUser) {
        log.info("删除水印, id={}",id);
        Optional<LiveWatermark> optional = liveWatermarkRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BizException(ResponseCode.WATERMARK_NOT_EXIST.getMsg());
        }
        LiveWatermark watermark = optional.get();
        if(Objects.nonNull(watermark.getUsedRoomNos())){
            throw new BizException(String.format(ResponseCode.WATERMARK_USED_ROOM.getMsg(), watermark.getUsedRoomNos()));
        }
        TxLiveWatermarkResponse result = txCloudLiveUtil.deleteWatermark(watermark.getWatermarkId());
        if(Objects.nonNull(result)){
            watermark.setWatermarkRequestId(result.getRequestId());
        }
        watermark.initDel(createUser);
        liveWatermarkRepository.save(watermark);
        return Boolean.TRUE;
    }

    /**
     * 添加直播间水印
     * @param param
     * @return
     */
    @Override
    public Boolean createLive(LiveRoomWatermarkParam param) {
        Long watermarkId = param.getWatermarkId();
        LiveWatermark watermark = liveWatermarkRepository.findTop1ByThirdIdAndWatermarkIdOrderByCreateDateTimeDesc(
                param.getThirdId(), watermarkId);
        if (Objects.isNull(watermark)) {
            throw new BizException(ResponseCode.WATERMARK_NOT_EXIST.getMsg());
        }
        String roomNo = param.getRoomNo();
        TxLiveWatermarkParam rxParam = TxLiveWatermarkParam.builder()
                .streamName(roomNo).watermarkId(watermarkId).build();
        TxLiveWatermarkResponse result = txCloudLiveUtil.createLiveWatermark(rxParam);
        if (Objects.isNull(result)) {
            throw new BizException(ResponseCode.WATERMARK_NOT_EXIST.getMsg());
        }
        watermark.setWatermarkLiveRequestId(result.getRequestId());

        StreamingRoomVO roomInfo = streamingRoomServiceImpl.detail(roomNo);
        LiveWatermark oldWatermark = null;
        if(Objects.nonNull(roomInfo.getWatermarkId()) && !Objects.equals(roomInfo.getWatermarkId(), -1L)
                && Objects.nonNull(oldWatermark = liveWatermarkRepository.findTop1ByWatermarkIdOrderByCreateDateTimeDesc(roomInfo.getWatermarkId()) )){
            log.info("删除直播间之前关联的水印.....");
            Set<String> oldWatermarkRoomNos = oldWatermark.getUsedRoomNos();
            oldWatermarkRoomNos.remove(roomNo);
            oldWatermark.setUsedRoomNos(oldWatermarkRoomNos);
            oldWatermark.initUpdate(param.getCreateUser());
            log.info("更新直播间之前的水印信息 oldWatermark={}", oldWatermark);
            liveWatermarkRepository.save(oldWatermark);
        }
        boolean b = streamingRoomServiceImpl.updateRoomWatermark(roomNo, watermarkId, param.getCreateUser());
        log.info("更新房間水印 update result={}", b);

        Set<String> usedRoomNos = watermark.getUsedRoomNos();
        usedRoomNos = (Objects.isNull(usedRoomNos)) ? new HashSet<String>(): usedRoomNos;
        usedRoomNos.add(roomNo);
        watermark.setUsedRoomNos(usedRoomNos);
        watermark.initUpdate(param.getCreateUser());
        log.info("更新水印信息 watermark={}", watermark);
        liveWatermarkRepository.save(watermark);
        return Boolean.TRUE;
    }

    /**
     * 取消直播间水印
     * @param param
     * @return
     */
    @Override
    public Boolean cancelLive(LiveRoomWatermarkParam param) {
        Long watermarkId = param.getWatermarkId();
        LiveWatermark watermark = liveWatermarkRepository.findTop1ByThirdIdAndWatermarkIdOrderByCreateDateTimeDesc(
                param.getThirdId(), watermarkId);
        if (Objects.isNull(watermark)) {
            throw new BizException(ResponseCode.WATERMARK_NOT_EXIST.getMsg());
        }
        String roomNo = param.getRoomNo();
        TxLiveWatermarkResponse result = txCloudLiveUtil.deleteLiveWatermark(roomNo);
        if(Objects.nonNull(result)){
            watermark.setWatermarkLiveRequestId(result.getRequestId());
        }
        boolean b = streamingRoomServiceImpl.updateRoomWatermark(roomNo, -1L, param.getCreateUser());
        log.info("更新房間水印 update result={}", b);

        Set<String> usedRoomNos = watermark.getUsedRoomNos();
        usedRoomNos.remove(roomNo);
        watermark.setUsedRoomNos(usedRoomNos);
        watermark.initUpdate(param.getCreateUser());
        log.info("更新水印信息 watermark={}", watermark);
        liveWatermarkRepository.save(watermark);
        return Boolean.TRUE;
    }

    /**
     * 取消所有使用改水印的直播间
     * @param id
     * @param createUser 操作员
     * @return
     */
    @Override
    public Boolean cancelWatermarkAll(String id, String createUser) {
        log.info("取消所有使用改水印的直播间, id={}",id);
        Optional<LiveWatermark> optional = liveWatermarkRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BizException(ResponseCode.WATERMARK_NOT_EXIST.getMsg());
        }
        LiveWatermark watermark = optional.get();
        Set<String> usedRoomNos = watermark.getUsedRoomNos();
        if(Objects.isNull(usedRoomNos)){
            return Boolean.TRUE;
        }
        Iterator<String> roomNos = usedRoomNos.iterator();
        while (roomNos.hasNext()) {
            String roomNo = roomNos.next();
            try {
                txCloudLiveUtil.deleteLiveWatermark(roomNo);
            } catch (Exception e){
                log.info("取消所有使用改水印的直播间-刪除直播水印失败 error={}", e.getMessage());
            }
            boolean b = streamingRoomServiceImpl.updateRoomWatermark(roomNo, -1L, watermark.getCreateUser());
            log.info("更新房間水印 update result={}", b);
        }
        watermark.setUsedRoomNos(new HashSet<>());
        watermark.initUpdate(createUser);
        log.info("取消所有使用改水印的直播间完成。更新水印信息 watermark={}", watermark);
        liveWatermarkRepository.save(watermark);
        return Boolean.TRUE;
    }
}
