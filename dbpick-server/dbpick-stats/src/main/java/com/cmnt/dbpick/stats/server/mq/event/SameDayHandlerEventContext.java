package com.cmnt.dbpick.stats.server.mq.event;

import com.cmnt.dbpick.stats.server.mq.service.RoomPlayRecordStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@DependsOn("sameDayHandlerEventProcessor")
public class SameDayHandlerEventContext {

    @Autowired
    private ApplicationContext applicationContext;
    //存放所有策略类Bean的map
    public static Map<SameDayEnum, Class<RoomPlayRecordStatsService>> strategyBeanMap= new HashMap<>();

    public RoomPlayRecordStatsService getStrategy(String day1, String day2){
        SameDayEnum dayEnum = SameDayEnum.getEnumByDate(day1, day2);
        Class<RoomPlayRecordStatsService> strategyClass = strategyBeanMap.get(dayEnum);
        if(strategyClass==null) {
            return null;
        }
        //从容器中获取对应的策略Bean
        return applicationContext.getBean(strategyClass);
    }
}
