package com.cmnt.dbpick.stats.server.event;

import com.cmnt.dbpick.common.enums.live.WatchRoomTypeEnum;
import java.lang.annotation.*;

/**
 * 策略类注解实现-观看类型
 */
@Target(ElementType.TYPE)  //作用在类上
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited  //子类可以继承此注解
public @interface WatchTypeEventType {
    /**
     * 策略类型
     * @return
     */
    WatchRoomTypeEnum value();
}
