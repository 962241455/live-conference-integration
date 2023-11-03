package com.cmnt.dbpick.stats.server.es;

import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.enums.SortEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.profile.ProfileShardResult;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ESClient
 */
@Component
@Slf4j
public class ESUtils {

    @Autowired
    private RestHighLevelClient client;

//    public void getClient(){
//        log.info("初始化ES客户端......ip={}:{}", EsConfig.ES_HOST, EsConfig.ES_PORT);
//        this.client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost( EsConfig.ES_HOST, EsConfig.ES_PORT,  EsConfig.ES_SCHEME)));
//    }

//    public void close() {
//        log.info("关闭ES客户端......");
//        if (client != null) {
//            try {
//                client.close();
//                log.info("关闭ES客户端完成");
//            } catch (IOException e) {
//                log.error("关闭ES客户端出现异常！", e);
//                e.printStackTrace();
//            }
//        }
//    }


    /**
     * 创建索引
     * @param indexName 要创建的索引的名字，传入如： testindex
     * @return 创建索引的响应对象。可以使用 {@link CreateIndexResponse#isAcknowledged()} 来判断是否创建成功。如果为true，则是创建成功
     */
    public CreateIndexResponse createIndex(String indexName) throws IOException {
//        getClient();
        log.info("创建索引......indexName={}",indexName);
        CreateIndexResponse response;
        if(existIndex(indexName)){
            response = new CreateIndexResponse(false, false, indexName);
            return response;
        }
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        response = client.indices().create(request, RequestOptions.DEFAULT);
        log.info("创建索引完成......response={}",response);
//        close();
        return response;
    }

    /**
     * 通过elasticsearch数据的id，来删除这条数据
     * @param indexName 索引名字
     */
    public Boolean deleteIndex(String indexName) {
        log.info("根据删除索引......indexName={}",indexName);
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);;
        try {
            if(!existIndex(indexName)){
               return Boolean.TRUE;
            }
            AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            return delete.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }


    /**
     * 判断索引是否存在
     * @param indexName 要判断是否存在的索引的名字，传入如： testindex
     * @return 返回是否存在。
     */
    public boolean existIndex(String indexName){
        log.info("判断索引是否存在......indexName={}",indexName);
        GetIndexRequest request = new GetIndexRequest(indexName);
        request.indices();
        boolean exists;
        try {
            exists = client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("判断索引是否存在出错......indexName={}",e.getMessage());
            e.printStackTrace();
            exists = false;
        }
        log.info("判断索引是否存在......indexName={},exists={}",indexName,exists);
        return exists;
    }

    /**
     * 数据添加，网 elasticsearch 中添加一条数据
     * @param dataJson 要增加的数据，key-value形式。
     * @param indexName 索引名字，类似数据库的表，是添加进那个表
     * @param id 要添加的这条数据的id, 如果传入null，则由es系统自动生成一个唯一ID
     * @return 创建结果。如果 {@link IndexResponse#getId()} 不为null、且id长度大于0，那么就成功了
     */
    public IndexResponse put(String indexName,String dataJson,String id){
//        getClient();
        //创建请求
        log.info("数据es添加......indexName={},id={},data={}",indexName,id,dataJson);
        IndexRequest request = new IndexRequest(indexName);
        if(id != null){
            request.id(id);
        }
        request.timeout(TimeValue.timeValueSeconds(5));
        request.source(JSONObject.toJSONString(dataJson), XContentType.JSON);
        IndexResponse response = null;
        try {
            response = client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("数据es添加完成......response={}",response);
//        close();
        return response;
    }

    /**
     * 数据添加，es系统自动生成一个唯一ID
     */
    public IndexResponse put(String indexName, String dataJson){
        return put(indexName,dataJson, null);
    }

    /**
     * 批量添加数据
     * @param dataJsons 批量添加的数据的List
     * @param indexName 索引名字，类似数据库的表，是添加进那个表
     * @return {@link BulkResponse} ，如果没提交，或者提交的是空，或者出错，那么会返回null。判断其有没有提交成功可以使用  (res != null && !res.hasFailures())
     */
    public void putBatch(String indexName,List<String> dataJsons, List<String> ids){
        puts(indexName, dataJsons, ids);
    }
    public BulkResponse puts(String indexName,List<String> dataJsons, List<String> ids){
//        getClient();
        log.info("批量添加数据......indexName={},size={}",indexName,dataJsons.size());
        if(dataJsons.size() < 1){
            return null;
        }
        //批量增加
        BulkRequest bulkAddRequest = new BulkRequest();
        IndexRequest indexRequest;
        for (int i = 0; i < dataJsons.size(); i++) {
            indexRequest = new IndexRequest(indexName);
            indexRequest.id(ids.get(i));
            indexRequest.source(dataJsons.get(i), XContentType.JSON);
            bulkAddRequest.add(indexRequest);
        }
        BulkResponse response = null;
        try {
            response = client.bulk(bulkAddRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("批量添加数据完成......response={}",response);
//        close();
        return response;
    }

    /**
     * 查询并分页
     * @param indexName 索引名字
     //* @param query 查询条件， {@link SearchSourceBuilder}
     * @param from 从第几条开始查询，相当于 limit a,b 中的a ，比如要从最开始第一条查，可传入： 0
     * @param size 本次查询最大查询出多少条数据 ,相当于 limit a,b 中的b
     * @return {@link SearchResponse} 结果，可以通过 response.status().getStatus() == 200 来判断是否执行成功
     */
    public SearchResponse search(String indexName, SearchSourceBuilder searchSourceBuilder, Integer from, Integer size){
//        getClient();
        SearchRequest request = new SearchRequest(indexName);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        request.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            log.info("查询索引数据......indexName={}, searchSourceBuilder={}",indexName,searchSourceBuilder);
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        close();
        return response;
    }
    public SearchResponse searchAllByPage(String indexName,Integer from, Integer size) {
        log.info("分页查询索引数据......indexName={}, from={}，size={}",indexName,from,size);
        return search(indexName,
                new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()), from, size);
    }

    /**
     * 将查询参数组装成 es 查询语句
     * @param fieldParam
     * @return
     */
    private SearchSourceBuilder handleFieldParam2SearchQuery(Map<String,Object> fieldParam){
        log.info("将查询参数组装成 es 查询语句, fieldParam={}",fieldParam);
        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        if(Objects.nonNull(fieldParam) && !fieldParam.isEmpty()){
            BoolQueryBuilder queryParam = QueryBuilders.boolQuery();
            fieldParam.forEach((k,v) -> queryParam.must(QueryBuilders.termQuery(k,v)));
            query.query(queryParam);
        }
        return query;
    }
    private SearchSourceBuilder handleFieldParam2SearchQueryRLike(Map<String,Object> fieldParam){
        log.info("将查询参数组装成 es 查询语句(右模糊查询), fieldParam={}",fieldParam);
        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        if(Objects.nonNull(fieldParam) && !fieldParam.isEmpty()){
            BoolQueryBuilder queryParam = QueryBuilders.boolQuery();
            fieldParam.forEach((k,v) -> queryParam.must(QueryBuilders.wildcardQuery(k,v+"*")));
            query.query(queryParam);
        }
        return query;
    }

    public SearchResponse searchAllByPage(String indexName, Map<String,Object> fieldParam,
                                          Integer from, Integer size) {
        log.info("按条件分页查询索引数据......indexName={}, fieldParam={}, from={}，size={}",
                indexName, fieldParam, from,size);
        SearchSourceBuilder query = handleFieldParam2SearchQuery(fieldParam);
        return search(indexName, query, from, size);
    }
    public SearchResponse searchAllByPageOrderDesc(String indexName, Map<String,Object> fieldParam,
                                               String orderField,Integer from, Integer size) {
        log.info("按条件分页查询索引数据......indexName={}, fieldParam={}, from={}，size={}",
                indexName, fieldParam, from,size);
        SearchSourceBuilder query = handleFieldParam2SearchQuery(fieldParam);
        query.sort(orderField, SortOrder.DESC);
        return search(indexName, query, from, size);
    }

    public SearchResponse searchAllByPage(String indexName, BoolQueryBuilder queryParam,
                                          Integer from, Integer size) {
        return searchAllByPageOrderDesc(indexName, queryParam,"", from, size);
    }

    public SearchResponse searchAllByPageOrderDesc(String indexName, BoolQueryBuilder queryParam,
                                          String orderField,Integer from, Integer size) {
        log.info("按条件分页查询索引数据(自定义查询条件)......" +
                        "indexName={}, queryParam={},orderField={}, from={}，size={}",
                indexName, queryParam, orderField,from,size);
        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        if(Objects.nonNull(queryParam)){
            query.query(queryParam);
        }
        if(StringUtils.isNotBlank(orderField)){
            query.sort(orderField, SortOrder.DESC);
        }
        return search(indexName, query, from, size);
    }


    public SearchResponse searchAllByPageOrderAsc(String indexName,String orderField,
                                                  Map<String,Object> fieldParam,
                                                  Integer from, Integer size) {
        log.info("分页查询索引数据(根据字段升序)......" +
                "indexName={}, fieldParam={}, from={}，size={},orderField={}"
                ,indexName,fieldParam,from,size,orderField);
        SearchSourceBuilder query = handleFieldParam2SearchQuery(fieldParam);
        if(StringUtils.isNotBlank(orderField)){
            query.sort(orderField, SortOrder.ASC);
        }
        return search(indexName, query, from, size);
    }
    public SearchResponse searchAllByPageOrderAscRLike(String indexName,String orderField,
                                                  Map<String,Object> fieldParam,
                                                  Integer from, Integer size) {
        log.info("分页右模糊查询索引数据(根据字段升序)......" +
                        "indexName={}, fieldParam={}, from={}，size={},orderField={}"
                ,indexName,fieldParam,from,size,orderField);
        SearchSourceBuilder query = handleFieldParam2SearchQueryRLike(fieldParam);
        if(StringUtils.isNotBlank(orderField)){
            query.sort(orderField, SortOrder.ASC);
        }
        return search(indexName, query, from, size);
    }
    public SearchResponse searchAllByPageOrderDesc(String indexName,String orderField,
                                                  Integer from, Integer size) {
        log.info("分页查询索引数据(根据字段降序)......" +
                "indexName={}, from={}，size={},orderField={}",indexName,from,size,orderField);
        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        if(StringUtils.isNotBlank(orderField)){
            query.sort(orderField, SortOrder.DESC);
        }
        return search(indexName, query, from, size);
    }

    /**
     * 根据条件查询索引前10条数据(根据字段排序)
     * @param indexName     索引
     * @param orderField    排序字段
     * @param orderBy       排序方式 asc-升序，desc-降序
     * @param fieldParam    查询条件
     * @return
     */
    public SearchResponse search10ByFieldOrder(
            String indexName,String orderField,String orderBy,Map<String,Object> fieldParam) {
        log.info("根据条件查询索引前10条数据(根据字段排序)...indexName={}, orderField={}，orderBy={},fieldParam={}",
                indexName,orderField,orderBy,fieldParam);
        SearchSourceBuilder query = handleFieldParam2SearchQuery(fieldParam);
        if(StringUtils.isNotBlank(orderField)){
            query.sort(orderField,
                    (StringUtils.isNotBlank(orderBy) && StringUtils.equals(orderBy,SortEnum.ASC.getValue()))
                    ?SortOrder.ASC:SortOrder.DESC);
        }
        return search(indexName, query, 0, 10);
    }

    /**
     * 更新索引配置
     * @param indexName         索引名
     * @param maxResultWindow   最大搜索值
     * @return
     */
    public Boolean updateIndexSettings(String indexName, Integer maxResultWindow){
        log.info("更新索引配置...indexName={}, maxResultWindow={}",indexName, maxResultWindow);
        AcknowledgedResponse response = null;
        try {
            //创建Setting对象
            Settings settings = Settings.builder()
                    .put("max_result_window", maxResultWindow)//es默认是10000
                    .build();
            //创建UpdateSettingsRequest
            UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest(settings, indexName);
            //执行put
            response = client.indices().putSettings(updateSettingsRequest, RequestOptions.DEFAULT);
            log.info("更新索引配置...response={}",response.isAcknowledged());
            return response.isAcknowledged();//打印返回结果   成功的话，返回结果是true
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 查询数据
     * @param indexName 索引名字
     * @param queryString 查询条件，传入如： name:guanleiming AND age:123
     * @param from 从第几条开始查询，相当于 limit a,b 中的a ，比如要从最开始第一条查，可传入： 0
     * @param size 本次查询最大查询出多少条数据 ,相当于 limit a,b 中的b
     * @param sort 排序方式。如果不需要排序，传入null即可。 比如要根据加入时间time由大到小，传入的便是： SortBuilders.fieldSort("time").order(SortOrder.DESC)
     * @return 查询的结果，封装成list返回。list中的每条都是一条结果。如果链接es出错或者查询异常又或者什么都没查出，那么都是返回一个 new ArrayList<Map<String,Object>>(); ，任何情况返回值不会为null
     * 		<p>返回的结果集中，每条会自动加入一项 esid ，这个是在es中本条记录的唯一id编号，es自动赋予的。</p>
     */
    public List<Map<String,Object>> search(String indexName, String queryString, Integer from, Integer size, SortBuilder sort){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if(queryString != null && queryString.length() > 0){
            //有查询条件，才会进行查询，否则会查出所有
            QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(queryString);
            searchSourceBuilder.query(queryBuilder);
        }

        //判断是否使用排序
        if(sort != null){
            searchSourceBuilder.sort(sort);
        }
        SearchResponse response = search(indexName, searchSourceBuilder, from, size);
        if(response.status().getStatus() == 200){
            SearchHit shs[] = response.getHits().getHits();
            for (int i = 0; i < shs.length; i++) {
                Map<String, Object> map = shs[i].getSourceAsMap();
                map.put("esid", shs[i].getId());
                list.add(map);
            }
        }else{
            //异常
        }
        return list;
    }


    /**
     * 查询数据
     * <p>如果数据超过100条，那么只会返回前100条数据。<p>
     * @param indexName 索引名字
     * @param queryString 查询条件，传入如： name:guanleiming AND age:123
     * @return 查询的结果，封装成list返回。list中的每条都是一条结果。如果链接es出错或者查询异常又或者什么都没查出，那么都是返回一个 new ArrayList<Map<String,Object>>(); ，任何情况返回值不会为null
     * 		<p>返回的结果集中，每条会自动加入一项 esid ，这个是在es中本条记录的唯一id编号，es自动赋予的。</p>
     */
    public List<Map<String,Object>> search(String indexName, String queryString){
        return search(indexName, queryString, 0, 100, null);
    }



    /**
     * 通过elasticsearch数据的id，获取这条数据
     * @param indexName 索引名字
     * @param id elasticsearch数据的id
     * @return 这条数据的内容。 如果返回null，则是没有找到这条数据，或者执行过程出错。
     */
    public Map<String,Object> searchById(String indexName, String id){
//        getClient();
        log.info("根据id查询数据......indexName={},id={}",indexName,id);
        GetRequest request = new GetRequest(indexName, id);
        GetResponse response = null;
        try {
            response = client.get(request, RequestOptions.DEFAULT);
            log.info("response={}",response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if(response.isSourceEmpty()){
            return null;
        }
        Map<String, Object> map = response.getSource();
        map.put("esid",response.getId());
        log.info("根据id查询数据完成......dataMap={}",map);
//        close();
        return map;
    }

    public Map searchById(String indexName){
//        getClient();
        log.info("根据索引查询数据......indexName={}",indexName);
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Map<String, ProfileShardResult> profileResults = response.getProfileResults();
        log.info("根据索引查询数据......results={}",profileResults);
//        close();
        return profileResults;
    }


    /**
     * 通过elasticsearch数据的id，来删除这条数据
     * @param indexName 索引名字
     * @param id 要删除的elasticsearch这行数据的id
     */
    public boolean deleteById(String indexName, String id) {
//        getClient();
        log.info("根据id删除数据......indexName={},id={}",indexName,id);
        DeleteRequest request = new DeleteRequest(indexName, id);
        DeleteResponse delete = null;
        try {
            delete = client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            //删除失败
            return false;
        } finally {
//            close();
        }
        if(delete == null){
            return false;
        }
        if(delete.getResult().equals(DocWriteResponse.Result.DELETED)){
            return true;
        }else{
            return false;
        }
    }



    /**
     * 删除 elasticsearch 数据
     * @param indexName 索引名字
     * @param queryParam 条件
     */
    public Boolean deleteIndexByQuery(String indexName, BoolQueryBuilder queryParam) {
        log.info("根据条件删除索引数据......indexName={}，queryParam={}",indexName,queryParam);
        try {
            if(!existIndex(indexName)){
                return Boolean.TRUE;
            }
            if(Objects.nonNull(queryParam)){
                DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
                request.setQuery(queryParam);
                BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);
                log.info("response={}",response);
                return Boolean.TRUE;
            }
            log.info("根据条件删除索引数据......查询条件为空");
            return Boolean.FALSE;
        } catch (IOException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    /**
     * 删除 elasticsearch 数据
     * @param indexName 索引名字
     * @param fieldParam 条件
     */
    public Boolean deleteIndexByFieldParam(String indexName, Map<String,Object> fieldParam) {
        log.info("根据条件删除索引数据......indexName={}，fieldParam={}",indexName,fieldParam);

        BoolQueryBuilder queryParam = QueryBuilders.boolQuery();
        if(Objects.nonNull(fieldParam) && !fieldParam.isEmpty()){
            fieldParam.forEach((k,v) -> queryParam.must(QueryBuilders.termQuery(k,v)));
            return deleteIndexByQuery(indexName, queryParam);
        }
        return Boolean.FALSE;
    }
    public Boolean deleteIndexByFieldParamRLike(String indexName, Map<String,String> fieldParam) {
        log.info("根据条件(右模糊)删除索引数据......indexName={}，fieldParam={}",indexName,fieldParam);
        if(Objects.nonNull(fieldParam) && !fieldParam.isEmpty()){
            BoolQueryBuilder queryParam = QueryBuilders.boolQuery();
            fieldParam.forEach((k,v) -> queryParam.must(QueryBuilders.wildcardQuery(k,v+"*")));
            return deleteIndexByQuery(indexName, queryParam);
        }
        return Boolean.FALSE;
    }



}
