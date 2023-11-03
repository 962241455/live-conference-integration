package com.cmnt.dbpick.common.aspect;

import cn.hutool.core.util.StrUtil;
import com.cmnt.dbpick.common.annotation.RedisLock;
import com.cmnt.dbpick.common.utils.RedissonUtil;
import com.cmnt.dbpick.common.utils.SpelUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description Redis分布式锁-切面
 *
 * @author shusong.liang
 * @version 1.0.0
 * @date 2022-08-10 10:14
 */
@Slf4j
@Aspect
@Component
public class RedisLockAspect {


    @Autowired
    private RedissonUtil redissonUtil;


    /** 分布式锁前缀 */
    private static final String REDISSON_LOCK_PREFIX = "redisson_lock:";

    @Around(value = "@annotation(redisLock)", argNames = "joinPoint,redisLock")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        log.info("进入 RedisLockAspect 切面");

        String lockKey = getRedisKey(joinPoint, redisLock.lockName(), redisLock.key());
        log.info("解析后：{}", lockKey);

        Object result = null;
        try {
            // 尝试获取锁，等待5秒，自己获得锁后一直不解锁则在指定时间后自动解锁
            boolean lock = redissonUtil.tryLock(lockKey, redisLock.timeUnit(), 5000, redisLock.expire());
            if (lock) {
                log.info("线程:{}，获取到了锁", Thread.currentThread().getName());
                log.info("======获得锁后进行相应的操作======" + Thread.currentThread().getName());
                //执行方法
                result = joinPoint.proceed();
                redissonUtil.unlock(lockKey);  //释放锁
                log.info("=========释放锁========" + Thread.currentThread().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将 spel 表达式转换为字符串
     *
     * @param joinPoint 切点
     * @return redisKey
     */
    private String getRedisKey(ProceedingJoinPoint joinPoint, String lockName, String spel) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object target = joinPoint.getTarget();
        Object[] arguments = joinPoint.getArgs();
        return REDISSON_LOCK_PREFIX + lockName + StrUtil.COLON + SpelUtils.parse(target, spel, targetMethod, arguments);
    }


}
