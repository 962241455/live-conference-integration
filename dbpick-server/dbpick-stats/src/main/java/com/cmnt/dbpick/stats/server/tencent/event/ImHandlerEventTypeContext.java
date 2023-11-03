package com.cmnt.dbpick.stats.server.tencent.event;

import com.cmnt.dbpick.stats.server.tencent.enums.ImCallbackEnum;
import com.cmnt.dbpick.stats.server.tencent.service.ImCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@DependsOn("imHandlerEventTypeProcessor")
public class ImHandlerEventTypeContext {

    @Autowired
    private ApplicationContext applicationContext;
    //存放所有策略类Bean的map
    public static Map<ImCallbackEnum, Class<ImCallbackService>> strategyBeanMap= new HashMap<>();

    public ImCallbackService getStrategy(String eventType){
        ImCallbackEnum callBackEnum = ImCallbackEnum.getEnum(eventType);
        Class<ImCallbackService> strategyClass = strategyBeanMap.get(callBackEnum);
        if(strategyClass==null) {
            return null;
        }
        //从容器中获取对应的策略Bean
        return applicationContext.getBean(strategyClass);
    }
}
