package com.cmnt.dbpick.common.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Component
public class RedisNoUtils {
 
    private static final Long SUCCESS = 1L;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 自增编号
     * @param key 编号开头
     * @return
     */
    public String getOrderCode(String key) {
        //如果值存在，直接自增返回        @第一个if
        if (this.hasKey(key)) {
            return getCode(key, this.incr(key).toString());
        }
        //拿不到值，说明可能到了零点，自增值失效，需要重新set 一个0进去
        //需要考虑分布式和本地并发问题，分布式通过redssion lock解决
        //本地并发可以通过lock 和 tryLock两种方案，lock是把while(true)放里面,本方案采用tryLock
        //while(true)里面两个分支，第一个分支是拿到锁，进去后需要考虑，执行成功后，临界区的线程进来所以需要先判空
        //第二个分支是拿不到锁，判断一下是否已经被拿到锁的线程set值成功，如果成功，直接返回
        boolean lock = this.getLock("CODE_LOCK_KEY" + key, key, 6000);
        try {
            while (true) {
                //拿到锁就初始化值和失效时间，没拿到锁就继续获取key的值
                if (lock) { // @第二个if
                    if (this.hasKey(key)) {  // @第三个if
                        this.set(key, 0, getNowToNextDayMilliseconds(), TimeUnit.MILLISECONDS);
                    }
                    return getCode(key, this.incr(key).toString());
                } else {
                    if (this.hasKey(key)) {// @第四个if
                        return getCode(key, this.incr(key).toString());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            this.releaseLock("CODE_LOCK_KEY" + key, key);
        }
    }


    /**
     * 生成编号
     * @param type 编号开头
     * @param number 编号
     * @return
     */
    private  String getCode(String type, String number) {
        String date = DateUtil.format(new Date(), "yyyyMMdd");
        StringBuffer buffer = new StringBuffer();
        buffer.append(type)
                .append(date);
        for (int i = number.length(); i < 6; i++) {
            buffer.append("0");
        }
        buffer.append(number);
        return buffer.toString();
    }
    /**
     * 今日剩余时间计算
     * @return
     */
    public Long getNowToNextDayMilliseconds() {
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        //当前天+1
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        //将时分秒毫秒都设为0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //减去当前时间获取插值
        return (calendar.getTimeInMillis() - System.currentTimeMillis());
    }
     /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return Optional.ofNullable(redisTemplate).map(template -> template.hasKey(key)).orElse(false);
    }
 
    /**
     * 普通自增
     * @param key 键
     * @return 值
     */
    public Long incr(String key){
        return redisTemplate.opsForValue().increment(key);
    }
 
    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间
     * @param timeUnit 类型
     * @return true成功 false 失败
     */
    public <T> boolean set(String key, T value, long time, TimeUnit timeUnit) {
        Optional.ofNullable(redisTemplate).map(template -> {
            if (time > 0) {
                template.opsForValue().set(key, value, time, timeUnit);
            } else {
                template.opsForValue().set(key, value);
            }
            return true;
        });
        return true;
    }
 
    /**
     * 获取锁
     *
     * @param lockKey         锁key
     * @param value           value
     * @param expireTime：单位-秒
     * @return boolean
     */
    public boolean getLock(String lockKey, String value, int expireTime) {
        return Optional.ofNullable(redisTemplate)
                .map(template -> template.opsForValue().setIfAbsent(lockKey, value, expireTime, TimeUnit.SECONDS))
                .orElse(false);
    }
 
    /**
     * 释放锁
     *
     * @param lockKey 锁key
     * @param value   value
     * @return boolean
     */
    public boolean releaseLock(String lockKey, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        return Optional.ofNullable(redisTemplate.execute(redisScript, Collections.singletonList(lockKey), value))
                .map(Convert::toLong)
                .filter(SUCCESS::equals)
                .isPresent();
    }
}