package com.cmnt.dbpick.live.server.tencent.event;

import com.cmnt.dbpick.live.server.tencent.enums.VodCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.service.VodCallBackService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VodHandlerEventTypeProcessor implements ApplicationContextAware {
    /**
     * 获取所有的策略Beanclass 加入HandlerContext属性中
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取所有策略注解的Bean
        Map<String, Object> eventTypeMap = applicationContext.getBeansWithAnnotation(VodHandlerEventType.class);
        eventTypeMap.forEach((k,v)->{
            Class<VodCallBackService> strategyClass = (Class<VodCallBackService>) v.getClass();
            VodCallBackEnum value = strategyClass.getAnnotation(VodHandlerEventType.class).value();
            //将class加入map中,type作为key
            VodHandlerEventTypeContext.strategyBeanMap.put(value,strategyClass);
        });
    }
}
