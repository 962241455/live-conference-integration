package com.cmnt.dbpick.live.server.mongodb.repository;

import com.cmnt.dbpick.live.api.vo.LiveRoomMenuVO;
import com.cmnt.dbpick.live.server.mongodb.document.LiveRoomMenu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 直播间菜单配置
 */
public interface LiveRoomMenuRepository extends MongoRepository<LiveRoomMenu, String> {

    List<LiveRoomMenuVO> findTop10ByRoomNoAndDeletedOrderByCreateDateTimeAsc(String roomNo, Boolean deleted);

    LiveRoomMenu findTop1ByRoomNoAndMenuNameAndDeletedOrderByCreateDateTimeAsc(String roomNo, String menuName, Boolean deleted);

}
