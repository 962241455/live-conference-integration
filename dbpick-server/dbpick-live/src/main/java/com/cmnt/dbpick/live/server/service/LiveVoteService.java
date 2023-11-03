package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.params.vote.CommitVoteInfoParam;
import com.cmnt.dbpick.live.api.params.vote.RoomVoteEditParam;
import com.cmnt.dbpick.live.api.params.vote.RoomVoteStatusEditParam;
import com.cmnt.dbpick.live.api.vo.vote.RoomVoteInfoVO;
import com.cmnt.dbpick.live.api.vo.vote.VoteInfoVO;
import com.cmnt.dbpick.live.server.mongodb.document.LiveVote;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * 直播投票信息相关接口
 */
public interface LiveVoteService {

    /**
     * 查询投票信息
     */
    PageResponse<RoomVoteInfoVO> list(StreamingRoomQueryParams param);

    /**
     * 添加/编辑投票信息
     * @param param
     * @return
     */
    RoomVoteInfoVO edit(RoomVoteEditParam param);

    /**
     * 删除投票信息
     * @param id
     * @param userId
     */
    Boolean deleteVote(String id, String userId);

    /**
     * 编辑投票状态
     * status 状态：publish_ing 发布，stop_publish 结束发布，again_publish 再次发布
     */
    RoomVoteInfoVO editVoteStatus(RoomVoteStatusEditParam param);

    /**
     * 发送im投票消息
     */
    void asyncSendImQuestionMsg(RoomVoteInfoVO vo);

    /**
     * 根据房间号停止所有投票信息
     */
    Boolean stopVoteByRoomNo(String roomNo, String createBy);

    /**
     * 提交投票信息
     */
    LiveVote commitVoteInfo(CommitVoteInfoParam param);
    //更新数据库投票信息
    void updVoteCommitTimes(LiveVote voteInfo, List<VoteInfoVO> commitAns);
    //发送im计数消息
    void sendImCounterMsg(LiveVote voteInfo);

    /**
     * 导出投票结果
     */
    void exportVoteResult(String roomNo, HttpServletResponse response) throws IOException;
}
