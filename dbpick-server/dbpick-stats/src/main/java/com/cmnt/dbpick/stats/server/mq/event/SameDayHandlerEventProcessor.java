package com.cmnt.dbpick.stats.server.mq.event;

import com.cmnt.dbpick.stats.server.mq.service.RoomPlayRecordStatsService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SameDayHandlerEventProcessor implements ApplicationContextAware {
    /**
     * 获取所有的策略Beanclass 加入HandlerContext属性中
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取所有策略注解的Bean
        Map<String, Object> eventTypeMap = applicationContext.getBeansWithAnnotation(SameDayHandlerEventType.class);
        eventTypeMap.forEach((k,v)->{
            Class<RoomPlayRecordStatsService> strategyClass = (Class<RoomPlayRecordStatsService>) v.getClass();
            SameDayEnum value = strategyClass.getAnnotation(SameDayHandlerEventType.class).value();
            //将class加入map中,type作为key
            SameDayHandlerEventContext.strategyBeanMap.put(value,strategyClass);
        });
    }
}
