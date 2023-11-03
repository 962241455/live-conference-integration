package com.cmnt.dbpick.live.server.tencent.event;

import com.cmnt.dbpick.live.server.tencent.enums.TrtcCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.service.TrtcCallBackService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrtcHandlerEventTypeProcessor implements ApplicationContextAware {
    /**
     * 获取所有的策略Beanclass 加入HandlerContext属性中
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取所有策略注解的Bean
        Map<String, Object> eventTypeMap = applicationContext.getBeansWithAnnotation(TrtcHandlerEventType.class);
        eventTypeMap.forEach((k,v)->{
            Class<TrtcCallBackService> strategyClass = (Class<TrtcCallBackService>) v.getClass();
            TrtcCallBackEnum value = strategyClass.getAnnotation(TrtcHandlerEventType.class).value();
            //将class加入map中,type作为key
            TrtcHandlerEventTypeContext.strategyBeanMap.put(value,strategyClass);
        });
    }
}
