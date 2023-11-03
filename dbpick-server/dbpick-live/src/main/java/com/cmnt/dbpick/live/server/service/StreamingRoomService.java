package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.user.StreamingRoomTimeVO;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.live.api.message.RoomPlayRecordMessage;
import com.cmnt.dbpick.live.api.params.*;
import com.cmnt.dbpick.live.api.vo.RoomHotOnlineVO;
import com.cmnt.dbpick.live.api.vo.redis.LiveStreamStopVO;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoom;

import java.util.List;

/**
 * 直播房间
 */
public interface StreamingRoomService {
    /**
     * 校验商户账号和凭证
     * @param thirdId
     * @return ak
     */
    String checkAccessKeyByThirdId(String thirdId);

    //查询直播房间列表
    PageResponse<StreamingRoomVO> list(StreamingRoomQueryParams params);
    /**
     * 查询用户所有直播房间号列表
     * @param param
     * @return
     */
    List<String> listByThird(ThirdRoomQueryParam param);


    /**
     * 新增直播房间
     */
    StreamingRoomVO add(StreamingRoomParams params);
    /**
     * 更新直播间信息
     */
    StreamingRoomVO update(StreamingRoomParams params);

    /**
     * 更新直播间状态
     * @param status 房间状态：no_live-未开播 live_ing-直播中 forbidden-房间封禁 live_pause-直播暂停 live_over-直播结束
     * @return
     */
    void updateRoomStatus(String roomNo, String status, String updateUser);

    /**
     * 删除直播房间
     * @param createUser 操作员
     */
    boolean deleteById(String roomId, String createUser);

    void refreshRoomCache(StreamingRoomVO roomVO);
    void refreshRoomCache(StreamingRoom room);
    StreamingRoomVO detail(String roomNo);
    /**
     * 根据id查询直播间详情
     */
    StreamingRoomVO info(String id);
    /**
     * 更新房间推流地址
     */
    StreamingRoomVO refreshRoomPushUrl(String roomNo);

    /**
     * 开启三方推流
     */
    Boolean openThirdPush(String roomNo);
    /**
     * 强制开启直播
     */
    Boolean startRoomLiveIng(String roomNo, String userId);

    //新增直播间 创建群组
    boolean createChatRoom(String roomNo, String userId);
    //解散聊天室
    boolean destroyChatRoom(String roomNo);

    /**
     * 禁言房间
     * @param param
     * @return
     */
    Boolean silencedChatRoom(RoomSilencedParam param);

    /**
     * 启动直播间
     * @param roomNo
     * @param createUser 操作员
     * @return
     */
    boolean resumeRoom(String roomNo, String createUser);
    /**
     * 关闭直播间
     * @param createUser 操作员
     * @param roomNo
     * @return
     */
    boolean dropRoom(String roomNo, String createUser);
    void forbidLiveStream(String roomNo);
    /**
     * 更新直播间水印
     * @param watermarkId 水印id
     * @return
     */
    boolean updateRoomWatermark(String roomNo, Long watermarkId, String updateUser);
    /**
     * 编辑直播间观看限制
     * @param param
     * @return
     */
    Boolean editWatchFilter(RoomWatchFilterParam param);

    /**
     * 处理直播停止之后的房间状态
     */
    Boolean stopLiveRoomStatus(LiveStreamStopVO liveStreamStopVO);
    /**
     * 更新直播间热度和在线人数
     */
    Boolean updRoomHotAndOnline(RoomHotOnlineVO param);

    /**
     * 查询多个房间信息
     */
    List<StreamingRoomTimeVO> getInfoByRoomNos(String roomNos);

    String pushAdminRedis(String pwd);
    void sendRoomPlayRecordMessage(RoomPlayRecordMessage msg);




    /**
     * 查询用户热度值
     * @param thirdId 商户id
     */
    String getThirdRedisHot(String thirdId);
    /**
     * 初始化用户热度值
     */
    String initThirdRedisHot(String thirdId);



}
