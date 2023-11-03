package com.cmnt.dbpick.common.utils;

import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 系统账号工具类
 */
@Slf4j
@Component
public class UserAccountUtil {

    public static final String ACCOUNT_PREFIX = "cid_";//账号前缀
    public static final Integer ACCOUNT_LENGTH = 15;//账号长度(不加前缀)
    public static final Integer MAX_TIMES = 5;//创建重试次数

    @Autowired
    private RedisUtils redisUtils;



    /**
     * 创建唯一账号
     */
    public String createUniqueAccount() {
        return createUniqueAccount(1);
    }

    public String createUniqueAccount(Integer times) {
        if(times > MAX_TIMES){
            throw new BizException(ResponseCode.HTTP_RES_CODE_500);
        }
        String account = AccessKeyUtils.upperOrLowerOrNumRandom(ACCOUNT_PREFIX, ACCOUNT_LENGTH);
        if(!redisUtils.isSetMember(RedisKey.BASE_ACCOUNT_INIT, account)){
            redisUtils.addSet(RedisKey.BASE_ACCOUNT_INIT, account);
            return account;
        }
        return createUniqueAccount(times+1);
    }





}
