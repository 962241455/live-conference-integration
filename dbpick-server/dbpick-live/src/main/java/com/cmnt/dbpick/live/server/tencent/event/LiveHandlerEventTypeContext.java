package com.cmnt.dbpick.live.server.tencent.event;

import com.cmnt.dbpick.live.server.tencent.enums.LiveCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.service.LiveCallBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@DependsOn("liveHandlerEventTypeProcessor")
public class LiveHandlerEventTypeContext {

    @Autowired
    private ApplicationContext applicationContext;
    //存放所有策略类Bean的map
    public static Map<LiveCallBackEnum, Class<LiveCallBackService>> strategyBeanMap= new HashMap<>();

    public LiveCallBackService getStrategy(Integer type){
        LiveCallBackEnum callBackEnum = LiveCallBackEnum.getEnum(type);
        Class<LiveCallBackService> strategyClass = strategyBeanMap.get(callBackEnum);
        if(strategyClass==null) {
            return null;
        }
        //从容器中获取对应的策略Bean
        return applicationContext.getBean(strategyClass);
    }
}
