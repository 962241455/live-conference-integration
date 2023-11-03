package com.cmnt.dbpick.live.server.tencent.event;


import com.cmnt.dbpick.live.server.tencent.enums.TrtcCallBackEnum;

import java.lang.annotation.*;

/**
 * 策略类注解实现
 */
@Target(ElementType.TYPE)  //作用在类上
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited  //子类可以继承此注解
public @interface TrtcHandlerEventType {
    /**
     * 策略类型
     * @return
     */
    TrtcCallBackEnum value();
}
