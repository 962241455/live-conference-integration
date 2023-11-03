package com.cmnt.dbpick.live.server.tencent.event;

import com.cmnt.dbpick.live.server.tencent.enums.TrtcCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.service.TrtcCallBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@DependsOn("trtcHandlerEventTypeProcessor")
public class TrtcHandlerEventTypeContext {

    @Autowired
    private ApplicationContext applicationContext;
    //存放所有策略类Bean的map
    public static Map<TrtcCallBackEnum, Class<TrtcCallBackService>> strategyBeanMap= new HashMap<>();

    public TrtcCallBackService getStrategy(Integer type){
        TrtcCallBackEnum callBackEnum = TrtcCallBackEnum.getEnum(type);
        Class<TrtcCallBackService> strategyClass = strategyBeanMap.get(callBackEnum);
        if(strategyClass==null) {
            return null;
        }
        //从容器中获取对应的策略Bean
        return applicationContext.getBean(strategyClass);
    }
}
