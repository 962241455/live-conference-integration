package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.model.RoomNoParam;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.LiveRoomMenuVO;
import com.cmnt.dbpick.live.server.mongodb.document.LiveRoomMenu;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveRoomMenuRepository;
import com.cmnt.dbpick.live.server.service.LiveRoomMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 直播间菜单设置
 */
@Slf4j
@Service
public class LiveRoomMenuServiceImpl implements LiveRoomMenuService {

    @Autowired
    private LiveRoomMenuRepository liveRoomMenuRepository;
    /**
     * 查询菜单设置列表
     * @param param
     * @return
     */
    @Override
    public List<LiveRoomMenuVO> list(RoomNoParam param) {
        List<LiveRoomMenuVO> list = new ArrayList<>();
        if(Objects.isNull(param) || StringUtils.isBlank(param.getRoomNo())){
            return list;
        }
        return liveRoomMenuRepository.findTop10ByRoomNoAndDeletedOrderByCreateDateTimeAsc(
                param.getRoomNo(), Boolean.FALSE);
    }

    /**
     * 添加/编辑菜单设置
     * @param param
     * @return
     */
    @Override
    public LiveRoomMenuVO edit(LiveRoomMenuParam param) {
        LiveRoomMenu roomMenu = null;
        if(StringUtils.isNotBlank(param.getId())){
            Optional<LiveRoomMenu> optional = liveRoomMenuRepository.findById(param.getId());
            if(!optional.isPresent()){
                throw new BizException("房间不存在该菜单。");
            }
            roomMenu = optional.get();
        }
        roomMenu = Objects.nonNull(roomMenu) ? roomMenu:
                liveRoomMenuRepository.findTop1ByRoomNoAndMenuNameAndDeletedOrderByCreateDateTimeAsc(
                        param.getRoomNo(),param.getMenuName(),Boolean.FALSE);
        if(Objects.isNull(roomMenu)){
            roomMenu = new LiveRoomMenu();
            FastBeanUtils.copy(param,roomMenu);
            roomMenu.initSave(param.getCreateUser());
        }else{
            roomMenu.setMenuContent(param.getMenuContent());
        }
        roomMenu.initUpdate(param.getCreateUser());
        roomMenu = liveRoomMenuRepository.save(roomMenu);
        LiveRoomMenuVO vo = new LiveRoomMenuVO();
        FastBeanUtils.copy(roomMenu,vo);
        return vo;
    }

    /**
     * 删除菜单设置
     * @param id
     * @param userId
     */
    @Override
    public Boolean deleteMenu(String id, String userId) {
        Optional<LiveRoomMenu> optional = liveRoomMenuRepository.findById(id);
        if(optional.isPresent()){
            LiveRoomMenu liveRoomMenu = optional.get();
            liveRoomMenu.initDel(userId);
            liveRoomMenuRepository.save(liveRoomMenu);
        }
        return Boolean.TRUE;
    }
}
