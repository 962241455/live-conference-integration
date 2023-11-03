package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.enums.ChoiceTypeEnum;
import com.cmnt.dbpick.common.enums.live.MessageStatusEnum;
import com.cmnt.dbpick.common.enums.live.MessageTypeEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.tx.tencent.TxCloudImUtil;
import com.cmnt.dbpick.common.tx.tencent.constant.TxCloudImConstant;
import com.cmnt.dbpick.common.tx.tencent.request.im.ImChatMessageParam;
import com.cmnt.dbpick.common.tx.tencent.request.im.ImNoticeMessageParam;
import com.cmnt.dbpick.common.user.StreamingRoomVO;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.live.api.params.vote.CommitVoteInfoParam;
import com.cmnt.dbpick.live.api.params.vote.RoomVoteEditParam;
import com.cmnt.dbpick.live.api.params.vote.RoomVoteStatusEditParam;
import com.cmnt.dbpick.live.api.vo.vote.RoomVoteInfoVO;
import com.cmnt.dbpick.live.api.vo.vote.VoteInfoVO;
import com.cmnt.dbpick.live.server.mongodb.document.LiveVote;
import com.cmnt.dbpick.live.server.mongodb.document.LiveVoteUserRecord;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveVoteRepository;
import com.cmnt.dbpick.live.server.mongodb.repository.LiveVoteUserRecordRepository;
import com.cmnt.dbpick.live.server.service.LiveVoteService;
import com.cmnt.dbpick.live.server.service.StreamingRoomService;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 直播投票信息
 */
@Slf4j
@Service
public class LiveVoteServiceImpl implements LiveVoteService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TxCloudImUtil txCloudImUtil;
    @Autowired
    private MongoPageHelper mongoPageHelper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private AccessAuthUtil accessAuthUtil;

    @Autowired
    private LiveVoteRepository liveVoteRepository;
    @Autowired
    private LiveVoteUserRecordRepository liveVoteUserRecordRepository;

    @Autowired
    private StreamingRoomService streamingRoomServiceImpl;

    private String getThirdIdByRoomNo(String roomNo){
        StreamingRoomVO roomInfo = streamingRoomServiceImpl.detail(roomNo);
        if(Objects.isNull(roomInfo)){
            throw new BizException(ResponseCode.AUCTION_ROOM_NULL);
        }
        return roomInfo.getThirdId();
    }

    /**
     * 查询投票信息
     */
    @Override
    public PageResponse<RoomVoteInfoVO> list(StreamingRoomQueryParams param) {
        Query query = new Query();
        if(StringUtils.isBlank(param.getRoomNo()) || StringUtils.isBlank(param.getThirdId())){
            throw new BizException("房间号/商户账号 不存在!", ResponseCode.FAILED);
        }
        //query.addCriteria(Criteria.where("status").ne(MessageStatusEnum.AGAIN_PUBLISH.getValue()));
        query.addCriteria(Criteria.where("roomNo").is(param.getRoomNo()));
        query.addCriteria(Criteria.where("thirdId").is(param.getThirdId()));
        log.info("查询投票信息列表 params={}", query);
        PageResponse<LiveVote> list = mongoPageHelper.pageQuery(query, LiveVote.class, param);
        PageResponse<RoomVoteInfoVO> response = JacksonUtils.toBean(JacksonUtils.toJson(list), PageResponse.class);

        return response;
    }

    /**
     * 添加/编辑投票信息
     * @param param
     * @return
     */
    @Override
    public RoomVoteInfoVO edit(RoomVoteEditParam param) {
        if(StringUtils.isBlank(param.getRoomNo())){
            throw new BizException("直播房间号 不能为空。");
        }
        if(StringUtils.isBlank(param.getTitle())){
            throw new BizException("投票标题 不能为空。");
        }
        LiveVote liveVote = null;
        if(StringUtils.isNotBlank(param.getId())){
            Optional<LiveVote> op = liveVoteRepository.findById(param.getId());
            if(!op.isPresent()){
                throw new BizException("未查询到投票信息。");
            }
            liveVote = op.get();
        }
        liveVote = editLiveVote(liveVote, param);
        RoomVoteInfoVO vo = new RoomVoteInfoVO();
        FastBeanUtils.copy(liveVote,vo);
        return vo;
    }

    private LiveVote editLiveVote(LiveVote liveVote, RoomVoteEditParam param){
        log.info("编辑投票信息 liveVote={},param={}", liveVote,param);
        Boolean newAnsId = Boolean.FALSE;
        if(Objects.isNull(liveVote)){
            liveVote = new LiveVote();
            String thirdId = StringUtils.isBlank(param.getThirdId())?
                    getThirdIdByRoomNo(param.getRoomNo()):param.getThirdId();
            liveVote.setAk(accessAuthUtil.getThirdAK(thirdId));
            liveVote.setThirdId(thirdId);
            liveVote.setMsgType(MessageTypeEnum.QUESTION.getValue());
            liveVote.setCommitTimes(0);
            liveVote.setStatus(MessageStatusEnum.NO_PUBLISH.getValue());
            liveVote.initSave(param.getCreateUser());
            liveVote = liveVoteRepository.save(liveVote);
            newAnsId = Boolean.TRUE;
        }
        VoteQuestion questionIdx = new VoteQuestion(0);
        List<VoteInfoVO> quesList = new ArrayList<>();
        Boolean finalNewAnsId = newAnsId;
        param.getVoteInfo().forEach(
                ques-> {
                    ques.setIdx(questionIdx.idx);
                    List<VoteInfoVO.VoteInfoItemVO> voteAnswers = new ArrayList<>();
                    VoteQuestion answerIdx = new VoteQuestion(0);
                    ques.getVoteAnswers().forEach(ans-> {
                        String ansId = ans.getId();
                        if(finalNewAnsId){
                            ansId = UUID.randomUUID().toString().replace("-","");
                        }
                        voteAnswers.add(new VoteInfoVO.VoteInfoItemVO(ansId , answerIdx.idx, ans.getAnswerItem()));
                        answerIdx.idx+=1;
                    });
                    ques.setVoteAnswers(voteAnswers);
                    quesList.add(ques);
                    questionIdx.idx+=1;
                }
        );
        liveVote.setRoomNo(param.getRoomNo());
        liveVote.setTitle(param.getTitle());
        liveVote.setVoteInfo(quesList);
        liveVote.setAllowGiveUp(param.getAllowGiveUp());
        liveVote.initUpdate(param.getCreateUser());
        log.info("保存投票信息 liveVote={}", param);
        liveVote = liveVoteRepository.save(liveVote);
        return liveVote;
    }


    /**
     * 删除投票信息
     * @param id
     * @param userId
     */
    @Override
    public Boolean deleteVote(String id, String userId) {
        Optional<LiveVote> op = liveVoteRepository.findById(id);
        if(op.isPresent()){
            LiveVote liveVote = op.get();
            liveVote.setStatus(MessageStatusEnum.STOP_PUBLISH.getValue());
            liveVote.initDel(userId);
            liveVoteRepository.save(liveVote);
        }
        return Boolean.TRUE;
    }


    /**
     * 编辑投票状态
     * status 状态：publish_ing 发布，stop_publish 结束发布，again_publish 再次发布
     */
    @Override
    public RoomVoteInfoVO editVoteStatus(RoomVoteStatusEditParam param){
        Optional<LiveVote> op = liveVoteRepository.findById(param.getVoteId());
        if(StringUtils.isBlank(param.getVoteId()) || !op.isPresent()){
            throw new BizException("未查询到投票信息。");
        }
        log.info("编辑投票状态 editVoteStatus param={}", param);
        LiveVote liveVote = op.get();
        RoomVoteInfoVO vo = new RoomVoteInfoVO();
        vo.setId(param.getVoteId());
        MessageStatusEnum statusEnum = MessageStatusEnum.getByValue(param.getStatus());
        switch (statusEnum){
            case PUBLISH_ING:
                log.info("发布投票信息状态 liveVote={}", liveVote);
                //TODO
                if(Objects.nonNull(
                        liveVoteRepository.findTop1ByRoomNoAndStatusOrderByCreateDateTimeDesc(
                                liveVote.getRoomNo(), MessageStatusEnum.PUBLISH_ING.getValue()))){
                    throw new BizException("同一时间只能有一个进行中的投票。");
                }
                liveVote.setStatus(param.getStatus());
                liveVote.setStartTime(DateUtil.getTimeStrampSeconds());
                liveVote.initUpdate(param.getCreateUser());
                liveVoteRepository.save(liveVote);
                FastBeanUtils.copy(liveVote,vo);
                break;
            case AGAIN_PUBLISH:
                log.info("再次发布投票信息 liveVote={}", liveVote);
                if(Objects.nonNull(liveVoteRepository.findTop1ByRoomNoAndStatusOrderByCreateDateTimeDesc(
                                liveVote.getRoomNo(), MessageStatusEnum.PUBLISH_ING.getValue()))){
                    throw new BizException("同一时间只能有一个进行中的投票。");
                }
                if(!StringUtils.equals(MessageStatusEnum.STOP_PUBLISH.getValue(),liveVote.getStatus())){
                    liveVote.setStatus(MessageStatusEnum.STOP_PUBLISH.getValue());
                    liveVote.initUpdate(param.getCreateUser());
                    liveVoteRepository.save(liveVote);
                }
                RoomVoteEditParam newParam = new RoomVoteEditParam();
                FastBeanUtils.copy(liveVote, newParam);
                LiveVote newVoteRecord = editLiveVote(null, newParam);
                newVoteRecord.setTitle(newParam.getTitle()+"(1)");
                newVoteRecord.setStatus(MessageStatusEnum.PUBLISH_ING.getValue());
                newVoteRecord.setStartTime(DateUtil.getTimeStrampSeconds());
                newVoteRecord.initUpdate(param.getCreateUser());
                newVoteRecord = liveVoteRepository.save(newVoteRecord);
                FastBeanUtils.copy(newVoteRecord,vo);
                break;
            case STOP_PUBLISH:
                liveVote.setStatus(param.getStatus());
                liveVote.initUpdate(param.getCreateUser());
                liveVoteRepository.save(liveVote);
                FastBeanUtils.copy(liveVote,vo);
                break;
            default:
                throw new BizException(MessageStatusEnum.UNKNOWN.getDesc());
        }
        return vo;
    }

    @Async
    @Override
    public void asyncSendImQuestionMsg(RoomVoteInfoVO vo){
        log.info("准备发送im投票消息，vo={}",vo);
        ImNoticeMessageParam msg = ImNoticeMessageParam.builder().msgKey(vo.getId()).groupId(vo.getRoomNo())
                .msgType(MessageTypeEnum.QUESTION.getValue()).msgContent(vo).build();
        txCloudImUtil.imGroupSystemPush(msg);

        //修改群组属性
        //txCloudImUtil.imGroupModifyField(msg);
        //重置属性
        txCloudImUtil.imGroupSetField(msg);

    }

    /**
     * 根据房间号停止所有投票信息
     */
    @Override
    public Boolean stopVoteByRoomNo(String roomNo, String createBy){
        log.info("根据房间号停止所有投票信息,roomNo={}",roomNo);
        LiveVote voteParam = LiveVote.builder().roomNo(roomNo).build();
        voteParam.setDeleted(Boolean.FALSE);
        List<LiveVote> voteList = liveVoteRepository.findAll(Example.of(voteParam));
        if(Objects.isNull(voteList) || voteList.isEmpty()){
            return Boolean.TRUE;
        }
        voteList.forEach(
                vote -> {
                    vote.setStatus(MessageStatusEnum.STOP_PUBLISH.getValue());
                    vote.initUpdate(createBy);
                }
        );
        log.info("更新的投票信息 voteList={}", voteList);
        liveVoteRepository.saveAll(voteList);
        return Boolean.TRUE;
    }


    /**
     * 提交投票信息
     */
    @Override
    public LiveVote commitVoteInfo(CommitVoteInfoParam param){
        log.info("提交投票信息 param={}", param);
        String voteId = param.getVoteId();
        Optional<LiveVote> op = liveVoteRepository.findById(voteId);
        if(StringUtils.isBlank(param.getRoomNo()) || StringUtils.isBlank(voteId) ){
            throw new BizException("直播房间号&投票id 不能为空。");
        }
        if(!op.isPresent()
                || StringUtils.equals(op.get().getStatus(),MessageStatusEnum.STOP_PUBLISH.getValue())){
            throw new BizException("投票不存在或已结束。");
        }
        LiveVote voteInfo = op.get();

        String userId = param.getCreateUser();
        String redisKey = String.format(RedisKey.ROOM_VOTE_COMMIT_USER,voteInfo.getRoomNo(),voteId, userId);
        Object incr = redisUtils.incrBy(redisKey, 1L);
        if(StringUtils.isBlank(userId) || Integer.parseInt(String.valueOf(incr))>1){
            throw new BizException("您已提交过该投票。");
        }
        redisUtils.expire(redisKey,15*24*3600L);

        LiveVoteUserRecord liveRecord = new LiveVoteUserRecord();
        liveRecord.setAk(voteInfo.getAk());
        liveRecord.setThirdId(voteInfo.getThirdId());
        liveRecord.setRoomNo(voteInfo.getRoomNo());
        liveRecord.setVoteId(voteId);
        liveRecord.setUserId(param.getCreateUser());
        liveRecord.setThirdUserId(param.getThirdUserId());
        liveRecord.setCommitInfo(param.getCommitInfo());
        liveRecord.initSave(param.getCreateUser());
        log.info("保存用户提交的投票信息 liveRecord={}", liveRecord);
        liveVoteUserRecordRepository.save(liveRecord);
        /*updVoteCommitTimes(voteInfo, param.getCommitInfo());
        sendImCounterMsg(voteInfo);*/
        return voteInfo;
    }

    @Async
    @Override
    public void updVoteCommitTimes(LiveVote voteInfo, List<VoteInfoVO> commitAns){
        log.info("投票信息={}, 提交人数+1, 投票id={}",voteInfo,voteInfo.getId());
        String commitRedisKey = String.format(RedisKey.ROOM_VOTE_COMMIT_TIMES, voteInfo.getRoomNo(),voteInfo.getId());
        Long redisCommitTimes = redisUtils.incrBy(commitRedisKey, 1L);
        redisUtils.expire(commitRedisKey,15*24*3600L);
        List<VoteInfoVO> redisVoteInfoVO = voteInfo.getVoteInfo();
        mongoTemplate.findAndModify(
                new Query(Criteria.where("_id").is(voteInfo.getId())),
                new Update().inc("commitTimes", 1).set("lastModifiedDate", ObjectTools.GetCurrentTime()),
                LiveVote.class);
        commitAns.forEach(
                ques -> {
                    Integer quesIdx = ques.getIdx();
                    //String ansId = ques.getVoteAnswers().get(0).getId();
                    ques.getVoteAnswers().forEach(
                            ans ->{
                                String ansId = ans.getId();
                                Query query = Query.query(Criteria.where("voteInfo.voteAnswers._id").is(ansId));
                                /*Update update = new Update().inc("voteInfo.$[].voteAnswers.$.choiceTimes", 1);*/
                                String updKey = "voteInfo."+quesIdx+".voteAnswers."+ans.getAnswerIdx()+".choiceTimes";
                                Update update = new Update().inc(updKey, 1);
                                UpdateResult updateResult = mongoTemplate.updateFirst(query, update, LiveVote.class);
                                log.info("更新投票信息提交人数，ansId={},updKey={},updateResult={}",ansId,updKey,updateResult);

                                String ansRedisKey = String.format(
                                        RedisKey.ROOM_VOTE_ANSWER_TIMES, voteInfo.getRoomNo(),voteInfo.getId(),quesIdx,ansId);
                                Long ansCommitTimes = redisUtils.incrBy(ansRedisKey, 1L);
                                redisUtils.expire(ansRedisKey,15*24*3600L);
                                redisVoteInfoVO.get(quesIdx).getVoteAnswers().get(ans.getAnswerIdx()).setChoiceTimes(ansCommitTimes.intValue());
                            }
                    );
                }
        );
        /*liveVoteRepository.save(voteInfo);*/
        voteInfo.setCommitTimes(redisCommitTimes.intValue());
        voteInfo.setVoteInfo(redisVoteInfoVO);
        sendImCounterMsg(voteInfo);
    }

    @Async
    @Override
    public void sendImCounterMsg(LiveVote voteInfo){
        log.info("准备发送im计数消息，vo={}",voteInfo);
        ImNoticeMessageParam msgVO = ImNoticeMessageParam.builder()
                .msgKey(voteInfo.getId()).groupId(voteInfo.getRoomNo())
                .msgType(MessageTypeEnum.COUNTER.getValue())
                .msgContent(voteInfo).build();
        log.info("组装im计数消息，msgVO={}",msgVO);
        ImChatMessageParam msg = ImChatMessageParam.builder()
                .syncOtherMachine(2)
                .toUserId(voteInfo.getCreateUser())
                .msgType(TxCloudImConstant.TIM_TEXT_ELEM)
                .msgKey(voteInfo.getId())
                .msgContent(msgVO)
                .build();
        //txCloudImUtil.imGroupSystemPush(msg);
        txCloudImUtil.sendMsg(msg);
    }


    @Override
    public void exportVoteResult(String voteId, HttpServletResponse response) {
        Optional<LiveVote> op = liveVoteRepository.findById(voteId);
        if(!op.isPresent()){
            throw new BizException("未查询到投票信息。");
        }
        LiveVote liveVote = op.get();
        StreamingRoomVO roomInfo = streamingRoomServiceImpl.detail(liveVote.getRoomNo());
        if(Objects.isNull(roomInfo)){
            throw new BizException(ResponseCode.AUCTION_ROOM_NULL);
        }

        XSSFWorkbook workbook =new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("投票结果");
        Row rowHead = sheet.createRow(0);
        rowHead.createCell(0).setCellValue("投票发起时间");
        rowHead.createCell(1).setCellValue(DateUtil.long2YMDHMS(liveVote.getCreateDateTime()));

        rowHead = sheet.createRow(1);
        rowHead.createCell(0).setCellValue("投票截至时间");
        rowHead.createCell(1).setCellValue(DateUtil.dateTime2Str(liveVote.getLastModifiedDate()));

        rowHead = sheet.createRow(2);
        rowHead.createCell(0).setCellValue("会议号");
        rowHead.createCell(1).setCellValue(liveVote.getRoomNo());

        rowHead = sheet.createRow(3);
        rowHead.createCell(0).setCellValue("会议名称");
        rowHead.createCell(1).setCellValue(roomInfo.getTitle());

        rowHead = sheet.createRow(4);
        rowHead.createCell(0).setCellValue("投票名称");
        rowHead.createCell(1).setCellValue(liveVote.getTitle());

        rowHead = sheet.createRow(5);
        rowHead.createCell(0).setCellValue("提交人数");
        rowHead.createCell(1).setCellValue(String.valueOf(liveVote.getCommitTimes()));

        LiveVoteUserRecord param = LiveVoteUserRecord.builder().voteId(voteId).build();
        param.setDeleted(Boolean.FALSE);
        List<LiveVoteUserRecord> userRecord = liveVoteUserRecordRepository.findAll(Example.of(param));


        Row rowContent = sheet.createRow(6);
        rowContent.createCell(0).setCellValue("观众id");
        rowContent.createCell(1).setCellValue("三方id");
        rowContent.createCell(2).setCellValue("提交时间");

        VoteQuestion questionIdx = new VoteQuestion(3);
        Map<String, Integer> questionIdxMap = new HashMap<>();
        liveVote.getVoteInfo().forEach(
                ques -> {
                    StringBuffer ansTitle = new StringBuffer();
                    ques.getVoteAnswers().forEach(
                            ansId->{
                                questionIdxMap.put(ansId.getId(),questionIdx.idx);
                                ansTitle.append("\n "+(ansId.getAnswerIdx()+1)+ ansId.getAnswerItem());
                            }
                    );
                    rowContent.createCell(questionIdx.idx).setCellValue(
                            "题目（"
                                    + ChoiceTypeEnum.getByValue(ques.getChoiceType()).getDesc()+"）："
                                    +ques.getQuestion()
                                    + "\n 选项：" + ansTitle
                    );
                    questionIdx.idx+=1;
                }
        );

        VoteQuestion rowAnsIdx = new VoteQuestion(7);
        userRecord.forEach(
                userAns -> {
                    Row rowAns = sheet.createRow(rowAnsIdx.idx);
                    rowAns.createCell(0).setCellValue(userAns.getUserId());
                    rowAns.createCell(1).setCellValue(userAns.getThirdUserId());
                    rowAns.createCell(2).setCellValue(DateUtil.long2YMDHMS(liveVote.getCreateDateTime()));
                    userAns.getCommitInfo().forEach(
                            commitInfo->{
                                commitInfo.getVoteAnswers().forEach(
                                        commitAns -> rowAns.createCell(questionIdxMap.get(commitAns.getId())).setCellValue((commitAns.getAnswerIdx()+1)+commitAns.getAnswerItem())
                                );
                            }
                    );
                    rowAnsIdx.idx+=1;
                }
        );
        OutputStream osOut=null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=《"+liveVote.getTitle()+"》投票结果.xlsx");
            response.setContentType("application/octet-stream;charset=UTF-8");
            osOut = response.getOutputStream();
            workbook.write(osOut);
            osOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                osOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
class VoteQuestion{
    int idx;
    VoteQuestion(int idx){
        this.idx = idx;
    }
}
