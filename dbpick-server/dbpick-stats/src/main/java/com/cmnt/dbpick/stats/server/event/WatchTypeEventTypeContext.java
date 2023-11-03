package com.cmnt.dbpick.stats.server.event;

import com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum;
import com.cmnt.dbpick.stats.server.service.WatchTypeAnalyseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@DependsOn("watchTypeEventTypeProcessor")
public class WatchTypeEventTypeContext {

    @Autowired
    private ApplicationContext applicationContext;
    //存放所有策略类Bean的map
    public static Map<WatchRoomTypeEnum, Class<WatchTypeAnalyseService>> strategyBeanMap= new HashMap<>();

    public WatchTypeAnalyseService getStrategy(String eventType){
        WatchRoomTypeEnum typeEnum = WatchRoomTypeEnum.getByValue(eventType);
        Class<WatchTypeAnalyseService> strategyClass = strategyBeanMap.get(typeEnum);
        if(strategyClass==null) {
            return null;
        }
        //从容器中获取对应的策略Bean
        return applicationContext.getBean(strategyClass);
    }
}
