package com.cmnt.dbpick.live.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.enums.SwitchEnum;
import com.cmnt.dbpick.common.enums.live.LiveWatchFilterTypeEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.WatchRegisterInfoVO;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomWatchFilter;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomWatchFilterRepository;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import com.cmnt.dbpick.live.server.service.WatchFilterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 直播观看限制
 */
@Slf4j
@Service
public class WatchFilterServiceImpl implements WatchFilterService {

    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;

    @Autowired
    private StreamingRoomWatchFilterRepository streamingRoomWatchFilterRepository;

    /**
     * 取消直播观看限制
     * @param param
     * @return
     */
    @Override
    public Boolean cancelFilter(RoomWatchFilterParam param) {
        param.setFilterType(LiveWatchFilterTypeEnum.NO_FILTER.getValue());
        return streamingRoomServiceImpl.editWatchFilter(param);
    }

    /**
     * 关闭游客观看
     * @param param
     * @return
     */
    @Override
    public Boolean closeVisitorWatch(RoomWatchFilterParam param) {
        param.setVisitorSwitch(SwitchEnum.CLOSE.getValue());
        return streamingRoomServiceImpl.editWatchFilter(param);
    }

    /**
     * 保存直播观看限制
     * @param param
     */
    @Override
    public Boolean saveFilter(RoomWatchFilterRegisterParam param) {
        RoomWatchFilterParam roomWatchFilterParam = new RoomWatchFilterParam();
        Boolean changeFlag = Boolean.FALSE;
        if(StringUtils.isNotBlank(param.getFilterType())){
            StreamingRoomWatchFilter watchFilter = streamingRoomWatchFilterRepository.findTop1ByRoomNoAndFilterTypeAndDeletedOrderByCreateDateDesc(
                    param.getRoomNo(), param.getFilterType(), Boolean.FALSE
            );
            String ak = streamingRoomServiceImpl.checkAccessKeyByThirdId(param.getThirdId());
            if(Objects.isNull(watchFilter)){
                watchFilter = StreamingRoomWatchFilter.builder()
                        .ak(ak).thirdId(param.getThirdId())
                        .roomNo(param.getRoomNo()).filterType(param.getFilterType()).build();
                watchFilter.initSave(param.getCreateUser());
            }
            WatchRegisterInfoVO vo = new WatchRegisterInfoVO();
            FastBeanUtils.copy(param, vo);
            watchFilter.setFilterInfoJson(JSON.toJSONString(vo));
            watchFilter.initUpdate(param.getCreateUser());
            streamingRoomWatchFilterRepository.save(watchFilter);

            roomWatchFilterParam.setFilterType(param.getFilterType());
            changeFlag = Boolean.TRUE;
        }
        if(StringUtils.isNotBlank(param.getVisitorSwitch())){
            roomWatchFilterParam.setVisitorSwitch(param.getVisitorSwitch());
            changeFlag = Boolean.TRUE;
        }
        if(changeFlag){
            roomWatchFilterParam.setRoomNo(param.getRoomNo());
            streamingRoomServiceImpl.editWatchFilter(roomWatchFilterParam);
        }
        return Boolean.TRUE;
    }

    /**
     * 查询直播观看限制
     * @param param
     * @return
     */
    @Override
    public WatchRegisterInfoVO findFilter(RoomWatchFilterParam param) {
        if(Objects.isNull(param) || StringUtils.isBlank(param.getRoomNo())){
            throw new BizException(ResponseCode.AUCTION_ROOM_NULL);
        }
        String filterType = param.getFilterType();
        if(StringUtils.isBlank(filterType)){
            StreamingRoomVO detail = streamingRoomServiceImpl.detail(param.getRoomNo());
            if(Objects.isNull(detail)){
                throw new BizException(ResponseCode.AUCTION_ROOM_NULL);
            }
            filterType = detail.getWatchFilter();
        }
        StreamingRoomWatchFilter watchFilter = streamingRoomWatchFilterRepository.findTop1ByRoomNoAndFilterTypeAndDeletedOrderByCreateDateDesc(
                param.getRoomNo(), filterType, Boolean.FALSE
        );
        if(Objects.isNull(watchFilter)){
            return null;
        }
        WatchRegisterInfoVO vo = JSONObject.toJavaObject(JSONObject.parseObject(watchFilter.getFilterInfoJson()), WatchRegisterInfoVO.class);
        vo.setRoomNo(param.getRoomNo());
        return vo;
    }
}
