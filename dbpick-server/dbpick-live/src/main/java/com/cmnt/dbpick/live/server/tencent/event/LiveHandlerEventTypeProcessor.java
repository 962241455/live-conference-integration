package com.cmnt.dbpick.live.server.tencent.event;

import com.cmnt.dbpick.live.server.tencent.enums.LiveCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.service.LiveCallBackService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LiveHandlerEventTypeProcessor implements ApplicationContextAware {
    /**
     * 获取所有的策略Beanclass 加入HandlerContext属性中
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取所有策略注解的Bean
        Map<String, Object> eventTypeMap = applicationContext.getBeansWithAnnotation(LiveHandlerEventType.class);
        eventTypeMap.forEach((k,v)->{
            Class<LiveCallBackService> strategyClass = (Class<LiveCallBackService>) v.getClass();
            LiveCallBackEnum value = strategyClass.getAnnotation(LiveHandlerEventType.class).value();
            //将class加入map中,type作为key
            LiveHandlerEventTypeContext.strategyBeanMap.put(value,strategyClass);
        });
    }
}
