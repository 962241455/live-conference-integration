package com.cmnt.dbpick.stats.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.utils.*;
import com.cmnt.dbpick.live.api.params.StreamingRoomQueryParams;
import com.cmnt.dbpick.stats.api.params.AnalyseWatchTimesParam;
import com.cmnt.dbpick.stats.api.params.QueryRoomStatsParams;
import com.cmnt.dbpick.stats.api.vo.es.RoomUserRecordNo;
import com.cmnt.dbpick.stats.api.vo.es.RoomUserRecordOnceNo;
import com.cmnt.dbpick.stats.api.vo.es.RoomUserRecordStatsNo;
import com.cmnt.dbpick.stats.api.vo.es.WatchTimesPageParam;
import com.cmnt.dbpick.stats.server.es.ESUtils;
import com.cmnt.dbpick.stats.server.es.EsConstant;
import com.cmnt.dbpick.stats.server.event.WatchTypeEventTypeContext;
import com.cmnt.dbpick.stats.server.service.WatchTimesStatsService;
import com.cmnt.dbpick.stats.server.service.WatchTypeAnalyseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class WatchTimesStatsServiceImpl implements WatchTimesStatsService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ESUtils esUtils;

    @Autowired
    private WatchTypeEventTypeContext eventTypeContext;

    /**
     * es数据分析
     */
    @Override
    @Async
    public void analyseWatchTimes(String roomNo, String ak) {
        checkAkWithRedis(ak);
        AnalyseWatchTimesParam param = AnalyseWatchTimesParam.builder()
                .roomNo(roomNo).watchType(WatchRoomTypeEnum.ALL.getValue()).build();
        analyseWatchTimes(param);
    }

    /**
     * es数据分析-管理端
     */
    @Override
    public Boolean analyseWatchTimes(AnalyseWatchTimesParam analyseParam) {
        WatchTimesPageParam param = getWatchTimesPageInfo();
        log.info("es数据分析 analyseParam={}， param={}", analyseParam, param);
        WatchTypeAnalyseService analyseService = eventTypeContext.getStrategy(analyseParam.getWatchType());
        analyseService.executeAnalyse(param, analyseParam.getRoomNo());
        return Boolean.TRUE;
    }
    @Override
    public RoomUserRecordOnceNo initRoomUserRecordOnceNo(RoomUserRecordNo enterRecord,
                                                         RoomUserRecordNo exitRecord,
                                                         String watchType,
                                                         String watchTimeStr, Long watchTime){
        RoomUserRecordOnceNo onceNo = new RoomUserRecordOnceNo();
        FastBeanUtils.copy(enterRecord,onceNo);
        onceNo.setActEnterTime(enterRecord.getActTime());
        onceNo.setActEnterTimeMillis(enterRecord.getActTimeMillis());
        onceNo.setActExitTime(exitRecord.getActTime());
        onceNo.setActExitTimeMillis(exitRecord.getActTimeMillis());
        onceNo.setWatchType(watchType);
        onceNo.setWatchTimesStr(watchTimeStr);
        onceNo.setWatchTimes(watchTime);
        onceNo.setThirdUserId(exitRecord.getThirdUserId());
        return onceNo;
    }

    /**
     *  查询分析完成的数据-管理端
     */
    @Override
    public PageResponse<RoomUserRecordStatsNo> stats(StreamingRoomQueryParams param, String ak) {
        checkAkWithRedis(ak);
        QueryRoomStatsParams queryParam = new QueryRoomStatsParams();
        FastBeanUtils.copy(param, queryParam);
        return stats(queryParam);
    }
    /** 查询分析完成的数据-管理端 */
    @Override
    public PageResponse<RoomUserRecordStatsNo> stats(QueryRoomStatsParams queryParam) {
        if(Objects.isNull(queryParam) || StringUtils.isBlank(queryParam.getRoomNo())){
            throw new BizException(ResponseCode.NO_ROOM_N0);
        }
        Integer pageNum = queryParam.getPageNum();
        Integer pageSize = queryParam.getPageSize();

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.wildcardQuery("roomNo", (queryParam.getRoomNo()+"*")));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(queryParam.getUserId())){
            queryBuilder.must(QueryBuilders.termQuery("userId",queryParam.getUserId()));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(queryParam.getThirdUserId())){
            queryBuilder.must(QueryBuilders.termQuery("thirdUserId",queryParam.getThirdUserId()));
        }
        List<RoomUserRecordStatsNo> statsList = new ArrayList<>();
        Long totalCount = 0L;
        try {
            SearchResponse result = esUtils.searchAllByPage(getWatchTimesPageInfo().getUserRecordStatsIdx(),
                    queryBuilder,(pageNum-1)*pageSize, pageSize);
            if(Objects.nonNull(result)){
                SearchHits hits = result.getHits();
                hits.forEach(data -> statsList.add(JSON.parseObject(data.getSourceAsString(), RoomUserRecordStatsNo.class)));
                totalCount = hits.getTotalHits().value;
            }
        } catch (Exception e){
            throw new BizException("索引不存在");
        }

        PageResponse<RoomUserRecordStatsNo> pageResponse = new PageResponse();
        pageResponse.setPageNum(pageNum);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalCount(totalCount);
        pageResponse.setTotalPage((int) (Math.ceil(totalCount / (double) pageSize)));
        pageResponse.setData(statsList);
        return pageResponse;
    }

    private void checkAkWithRedis(String ak) {
        Object redisAk = redisUtils.get(RedisKey.ES_WATCH_TIMES_AK);
        log.info("获取到redisAk值为： {}", redisAk);
        if(Objects.isNull(redisAk) || !StringUtils.equals(ak,String.valueOf(redisAk))){
            throw new BizException(ResponseCode.NOT_FIND_ACCESS);
        }
    }
    // 获取 redis 查询观看时长参数
    public WatchTimesPageParam getWatchTimesPageInfo() {
        String redisKey = RedisKey.ES_WATCH_TIMES_SEARCH_PAGE_INFO;
        Object object = redisUtils.get(redisKey);
        log.info("获取 redis 查询观看时长参数: key={},Object={}", redisKey, object);
        WatchTimesPageParam param = new WatchTimesPageParam();
        if (Objects.isNull(object)) {
            param = WatchTimesPageParam.builder()
                    .userRecordOnceIdx(EsConstant.ES_INDEX_ROOM_USER_RECORD_ONCE)
                    .userRecordStatsIdx(EsConstant.ES_INDEX_ROOM_USER_RECORD_STATS)
                    .userRecordIdx(EsConstant.ES_INDEX_ROOM_USER_RECORD)
                    .maxPage(EsConstant.ES_WATCH_TIMES_PAGE_NO_DEFAULT)
                    .pageSize(EsConstant.ES_WATCH_TIMES_PAGE_SIZE_DEFAULT).build();
            log.info("获取 redis 查询观看时长参数:累计查询 page={}* size={} 条数据，后续可在redis配置更改",
                    EsConstant.ES_WATCH_TIMES_PAGE_NO_DEFAULT, EsConstant.ES_WATCH_TIMES_PAGE_SIZE_DEFAULT);
            redisUtils.set(redisKey, JSON.toJSONString(param));
            return param;
        }
        return JSONObject.toJavaObject(JSONObject.parseObject(object.toString()), WatchTimesPageParam.class);
    }

    @Override
    public void setRedisKey(String redisKey) {
        redisUtils.set(RedisKey.ES_WATCH_TIMES_AK, redisKey);
    }


    /**
     * 导出观看时长
     */
    @Override
    public void exportStatsData(String roomNo, HttpServletResponse response) {
        OutputStream osOut=null;
        try {
            SearchResponse result = esUtils.searchAllByPageOrderAscRLike(
                    getWatchTimesPageInfo().getUserRecordStatsIdx()
                    ,"",
                    new HashMap<String,Object>(){{put("roomNo", roomNo);}},0, 10000);
            if(Objects.nonNull(result)){
                XSSFWorkbook workbook =new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("观看时长");
                Row rowHead = sheet.createRow(0);
                rowHead.createCell(0).setCellValue("用户id");
                rowHead.createCell(1).setCellValue("三方id");
                rowHead.createCell(2).setCellValue("观看类型");
                rowHead.createCell(3).setCellValue("观看时长(秒)");
                rowHead.createCell(4).setCellValue("观看时长");
                SearchHits hits = result.getHits();
                AtomicInteger rowIdx = new AtomicInteger(1);
                hits.forEach(data -> {
                    Row rowContent = sheet.createRow(rowIdx.get());
                    RoomUserRecordStatsNo statsInfo = JSON.parseObject(data.getSourceAsString(), RoomUserRecordStatsNo.class);
                    rowContent.createCell(0).setCellValue(statsInfo.getUserId());
                    rowContent.createCell(1).setCellValue(statsInfo.getThirdUserId());
                    rowContent.createCell(2).setCellValue(WatchRoomTypeEnum.getByValue(statsInfo.getWatchType()).getDesc());
                    rowContent.createCell(3).setCellValue(String.valueOf(statsInfo.getWatchTotalTimes()/1000));
                    rowContent.createCell(4).setCellValue(statsInfo.getWatchTotalTimesStr());
                    rowIdx.addAndGet(1);
                });
                response.setHeader("Content-Disposition", "attachment;filename=房间"+roomNo+"观看时长.xlsx");
                response.setContentType("application/octet-stream;charset=UTF-8");
                osOut = response.getOutputStream();
                workbook.write(osOut);
                osOut.flush();
            }
        } catch (Exception e){
            throw new BizException("索引不存在");
        } finally {
            try {
                IOUtils.close(osOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
