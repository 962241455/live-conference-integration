package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.enums.SwitchEnum;
import com.cmnt.dbpick.common.enums.live.MessageTypeEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.tx.tencent.TxCloudImUtil;
import com.cmnt.dbpick.common.tx.tencent.request.im.ImNoticeMessageParam;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.common.utils.JacksonUtils;
import com.cmnt.dbpick.live.api.params.LiveRoomSceneParam;
import com.cmnt.dbpick.live.api.params.LiveRoomSceneSwitchParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.vo.AppendixInfoVO;
import com.cmnt.dbpick.live.api.vo.LiveRoomSceneVO;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomScene;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomSceneRepository;
import com.cmnt.dbpick.live.server.service.LiveRoomSceneService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 直播间菜单设置
 */
@Slf4j
@Service
public class LiveRoomSceneServiceImpl implements LiveRoomSceneService {

    @Autowired
    private MongoPageHelper mongoPageHelper;
    @Autowired
    private TxCloudImUtil txCloudImUtil;

    @Autowired
    private StreamingRoomSceneRepository streamingRoomSceneRepository;

    /**
     * 添加直播现场
     */
    @Override
    public LiveRoomSceneVO addScene(LiveRoomSceneParam param) {
        log.info("添加直播现场 param={}", param);
        StreamingRoomScene roomScene = new StreamingRoomScene();
        FastBeanUtils.copy(param,roomScene);
        roomScene.setAppendixList(new HashSet<AppendixInfoVO>() {{ addAll(param.getAppendixList()); }});
        roomScene.setStatusSwitch(SwitchEnum.CLOSE.getValue());
        roomScene.initSave(param.getCreateUser());
        roomScene = streamingRoomSceneRepository.save(roomScene);
        LiveRoomSceneVO vo = new LiveRoomSceneVO();
        FastBeanUtils.copy(roomScene,vo);
        return vo;
    }


    /**
     * 查询直播现场数据列表
     */
    @Override
    public PageResponse<LiveRoomSceneVO> getScenePageList(StreamingRoomQueryParams param) {
        log.info("查询直播现场数据列表 param={}", param);
        if(StringUtils.isBlank(param.getRoomNo()) || StringUtils.isBlank(param.getThirdId())){
            throw new BizException("房间号/商户账号 不存在!", ResponseCode.FAILED);
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("roomNo").is(param.getRoomNo()));
        log.info("查询直播现场数据列表 params={}", query);
        PageResponse<StreamingRoomScene> list = mongoPageHelper.pageQuery(query, StreamingRoomScene.class, param);
        PageResponse<LiveRoomSceneVO> response = JacksonUtils.toBean(JacksonUtils.toJson(list), PageResponse.class);
        return response;
    }


    /**
     * 更新上下架状态
     */
    @Override
    public LiveRoomSceneVO updateSceneStatus(LiveRoomSceneSwitchParam param) {
        log.info("更新上下架状态 param={}", param);
        Optional<StreamingRoomScene> roomScene = streamingRoomSceneRepository.findById(param.getId());
        if (!roomScene.isPresent()) {
            throw new BizException("直播现场数据不存在!", ResponseCode.FAILED);
        }
        StreamingRoomScene scene = roomScene.get();
        LiveRoomSceneVO vo = new LiveRoomSceneVO();
        if(Objects.nonNull(param.getStatusSwitch())
                && !StringUtils.equals(scene.getStatusSwitch(),param.getStatusSwitch())){
            scene.setStatusSwitch(param.getStatusSwitch());
            scene.initUpdate(param.getCreateUser());
            scene = streamingRoomSceneRepository.save(scene);
        }
        FastBeanUtils.copy(scene,vo);
        return vo;
    }


    /**
     * 查询可用的直播现场列表数据
     */
    @Override
    public List<LiveRoomSceneVO> usefulList(RoomNoParam param) {
        log.info("查询可用的直播现场列表数据 roomNoParam={}", param);
        if(Objects.isNull(param) || StringUtils.isBlank(param.getRoomNo())){
            return new ArrayList<>();
        }

        StreamingRoomQueryParams queryParam = new StreamingRoomQueryParams();
        queryParam.setRoomNo(param.getRoomNo());
        queryParam.setStatus(SwitchEnum.OPEN.getValue());
        queryParam.setSearchStartTime(DateUtil.fromDate2Str(new Date()));
        Query query = new Query();
        query.addCriteria(Criteria.where("roomNo").is(queryParam.getRoomNo()));
        query.addCriteria(Criteria.where("statusSwitch").is(queryParam.getStatus()));
        query.addCriteria(Criteria.where("overTime").gte(queryParam.getSearchStartTime()));

        log.info("查询可用的直播现场列表数据 query={}", query);
        PageResponse<StreamingRoomScene> allScene = mongoPageHelper.pageQuery(query, StreamingRoomScene.class, queryParam);
        log.info("查询可用的直播现场列表数据 result={}", allScene);
        if(Objects.isNull(allScene) || Objects.isNull(allScene.getData()) || allScene.getData().isEmpty()){
            return new ArrayList<>();
        }
        return allScene.getData().stream().map(data ->
                LiveRoomSceneVO.builder().id(data.getId()).msgDesc(data.getMsgDesc()).overTime(data.getOverTime())
                        .appendixList(data.getAppendixList())
                        .statusSwitch(data.getStatusSwitch()).createDateTime(data.getCreateDateTime()).build()
                ).collect(Collectors.toList());
    }

    /**
     * 发送直播现场im消息
     */
    @Async
    @Override
    public void asyncSendImSceneMsg(RoomNoParam param){
        log.info("准备发送直播现场im消息，param={}",param);
        List<LiveRoomSceneVO> voList = usefulList(param);
        log.info("准备发送直播现场im消息，voList={}",voList);
        ImNoticeMessageParam msg = ImNoticeMessageParam.builder()
                .msgKey(param.getRoomNo()).groupId(param.getRoomNo())
                .msgType(MessageTypeEnum.LIVE_SCENE.getValue()).msgContent(voList).build();
        txCloudImUtil.imGroupSystemPush(msg);
    }

}
