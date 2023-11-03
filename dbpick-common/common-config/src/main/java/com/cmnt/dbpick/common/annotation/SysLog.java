package com.cmnt.dbpick.common.annotation;

import com.cmnt.dbpick.common.enums.BusinessType;

import java.lang.annotation.*;

/**
 * @author liang
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {



    /**
     * 自定义模块名，eg:登录
     * @return
     */
    String title() default "";

    /**
     * 方法传入的参数
     * @return
     */
    String optParam() default "";

    /**
     * 操作类型，eg:INSERT, UPDATE...
     * @return
     * @see com.cmnt.dbpick.common.enums.BusinessType
     */
    BusinessType businessType() default BusinessType.OTHER;

}
