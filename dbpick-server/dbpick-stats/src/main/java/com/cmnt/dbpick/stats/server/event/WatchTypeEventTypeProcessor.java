package com.cmnt.dbpick.stats.server.event;

import com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum;
import com.cmnt.dbpick.stats.server.service.WatchTypeAnalyseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class WatchTypeEventTypeProcessor implements ApplicationContextAware {
    /**
     * 获取所有的策略Beanclass 加入HandlerContext属性中
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取所有策略注解的Bean
        Map<String, Object> eventTypeMap = applicationContext.getBeansWithAnnotation(WatchTypeEventType.class);
        log.info("WatchTypeEventTypeProcessor>>>获取所有策略注解的Bean:{}",eventTypeMap);
        eventTypeMap.forEach((k,v)->{
            Class<WatchTypeAnalyseService> strategyClass = (Class<WatchTypeAnalyseService>) v.getClass();
            WatchRoomTypeEnum value = strategyClass.getAnnotation(WatchTypeEventType.class).value();
            //将class加入map中,type作为key
            WatchTypeEventTypeContext.strategyBeanMap.put(value,strategyClass);
        });
    }
}
