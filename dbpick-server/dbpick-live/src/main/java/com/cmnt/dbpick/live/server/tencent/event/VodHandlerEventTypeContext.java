package com.cmnt.dbpick.live.server.tencent.event;

import com.cmnt.dbpick.live.server.tencent.enums.VodCallBackEnum;
import com.cmnt.dbpick.live.server.tencent.service.VodCallBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@DependsOn("vodHandlerEventTypeProcessor")
public class VodHandlerEventTypeContext {

    @Autowired
    private ApplicationContext applicationContext;
    //存放所有策略类Bean的map
    public static Map<VodCallBackEnum, Class<VodCallBackService>> strategyBeanMap= new HashMap<>();

    public VodCallBackService getStrategy(String type){
        VodCallBackEnum callBackEnum = VodCallBackEnum.getEnum(type);
        Class<VodCallBackService> strategyClass = strategyBeanMap.get(callBackEnum);
        if(strategyClass==null) {
            return null;
        }
        //从容器中获取对应的策略Bean
        return applicationContext.getBean(strategyClass);
    }
}
