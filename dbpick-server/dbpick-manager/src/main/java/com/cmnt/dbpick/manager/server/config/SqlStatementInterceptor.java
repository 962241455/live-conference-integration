package com.cmnt.dbpick.manager.server.config;

import com.cmnt.dbpick.common.utils.DateUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 *  mybatis执行sql耗时
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class,
                Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
                Object.class, RowBounds.class, ResultHandler.class, CacheKey.class,BoundSql.class})})
@SuppressWarnings({"unchecked", "rawtypes"})
public class SqlStatementInterceptor implements Interceptor {
    public static final Logger log = LoggerFactory.getLogger("sys-sql");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = DateUtil.getTimeStrampSeconds();
        try {
            return invocation.proceed();
        } finally {
            long timeConsuming = DateUtil.getTimeStrampSeconds()-startTime;
            log.info("执行SQL:{}ms", timeConsuming);
            if(timeConsuming>999&&timeConsuming<5000){
                log.info("执行SQL大于1s:{}ms", timeConsuming);
            }else if(timeConsuming>=5000&&timeConsuming<10000){
                log.info("执行SQL大于5s:{}ms", timeConsuming);
            }else if(timeConsuming>=10000){
                log.info("执行SQL大于10s:{}ms", timeConsuming);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
