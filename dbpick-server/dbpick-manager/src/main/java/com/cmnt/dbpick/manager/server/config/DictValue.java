package com.cmnt.dbpick.manager.server.config;

import java.lang.annotation.*;

/**
 * 自定义注解防止表单重复提交
 *
 *  *
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictValue
{

    String value() default "";

    String splitStr() default ",";

    String setField() default "";



}
