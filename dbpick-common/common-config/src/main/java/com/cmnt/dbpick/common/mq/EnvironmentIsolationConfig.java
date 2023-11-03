package com.cmnt.dbpick.common.mq;

import com.cmnt.dbpick.common.mq.properties.RocketEnhanceProperties;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;

public class EnvironmentIsolationConfig implements BeanPostProcessor {
    private RocketEnhanceProperties rocketEnhanceProperties;

    public EnvironmentIsolationConfig(RocketEnhanceProperties rocketEnhanceProperties) {
        this.rocketEnhanceProperties = rocketEnhanceProperties;
    }


    /**
     * 在装载Bean之前实现参数修改
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultRocketMQListenerContainer) {
            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;
            if (rocketEnhanceProperties.isEnabledIsolation() && StringUtils.hasText(rocketEnhanceProperties.getEnvironment())) {
                container.setTopic(String.join("_", container.getTopic(), rocketEnhanceProperties.getEnvironment()));
                container.setConsumerGroup(String.join("_", container.getConsumerGroup(), rocketEnhanceProperties.getEnvironment()));
            }
            return container;
        }
        return bean;
    }
}