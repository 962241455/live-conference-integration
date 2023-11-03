package com.cmnt.dbpick.manager.server.config;

import com.cmnt.dbpick.common.enums.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description 这里切换读/写模式
 * 原理是利用ThreadLocal保存当前线程是否处于读模式（通过开始READ_ONLY注解在开始操作前设置模式为读模式，
 * 操作结束后清除该数据，避免内存泄漏，同时也为了后续在该线程进行写操作时任然为读模式
 * @author fxb
 * @date 2018-08-31
 */
@Slf4j
public class DBContextHolder {

    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();

    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void set(DBTypeEnum dbType) {
        contextHolder.set(dbType);
    }

    public static DBTypeEnum get() {
//        log.info("切换数据源");
        DBTypeEnum dbTypeEnum = contextHolder.get();
        return dbTypeEnum;
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
//        log.info("切换到master");
    }

    public static void slave() {
// 轮询  若有多个从库 数据源，可做分流降压
        int index = counter.getAndIncrement() % 2;
        if (counter.get() > 9999) {
            counter.set(-1);
        }
        //if (index == 0) {
        set(DBTypeEnum.SLAVE1);
//        log.info("切换到slave1");
        /*}else {
            set(DBTypeEnum.SLAVE2);
            System.out.println("切换到slave2");
        }*/
    }
    public static void clearDbType() {
        contextHolder.remove();
    }

}
