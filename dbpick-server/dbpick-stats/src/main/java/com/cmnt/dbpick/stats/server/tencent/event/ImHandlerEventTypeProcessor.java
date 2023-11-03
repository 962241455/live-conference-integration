package com.cmnt.dbpick.stats.server.tencent.event;

import com.cmnt.dbpick.stats.server.tencent.enums.ImCallbackEnum;
import com.cmnt.dbpick.stats.server.tencent.service.ImCallbackService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ImHandlerEventTypeProcessor implements ApplicationContextAware {
    /**
     * 获取所有的策略Beanclass 加入HandlerContext属性中
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取所有策略注解的Bean
        Map<String, Object> eventTypeMap = applicationContext.getBeansWithAnnotation(ImHandlerEventType.class);
        eventTypeMap.forEach((k,v)->{
            Class<ImCallbackService> strategyClass = (Class<ImCallbackService>) v.getClass();
            ImCallbackEnum value = strategyClass.getAnnotation(ImHandlerEventType.class).value();
            //将class加入map中,type作为key
            ImHandlerEventTypeContext.strategyBeanMap.put(value,strategyClass);
        });
    }
}
