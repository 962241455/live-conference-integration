package com.cmnt.dbpick.common.aspect;

import com.alibaba.fastjson.JSON;
import com.cmnt.dbpick.common.utils.RSAUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description 加解密切面
 * @date 2022-08-24 11:48
 */
@Aspect
@Component
public class EncKeyAspect {
    //加密用的公钥，是上面生成密钥对方法生成的
    public static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALJi7IanVfq-gj1-vq8MZ4n1xQbMhs1iPbXWXubCviIm-nDZ6Z1O7c7GkWwaE7eZIzGSoFSHcRnQ5I59dnTF--sCAwEAAQ";

    //定义切点，指向上面定义的CompanyConditionAnno注解
    @Pointcut("@annotation(com.cmnt.dbpick.common.annotation.CompanyConditionAnno)")
    public void companyCodeCondition() {
    }

    //采用环绕通知方式，对此切点切入的地方进行增强。简单说就是可以在方法执行前，修改方法的一系列参数，在方法执行后，对方法返回值进行修改，强的一批。
    @Around(value = " companyCodeCondition()  ")
    public String insert(ProceedingJoinPoint pc) throws Throwable {
        MethodSignature signature = (MethodSignature) pc.getSignature();
        //获取切入方法的对象
        Method method = signature.getMethod();
        //获取参数值
        Object[] args = pc.getArgs();
        //获取运行时参数的名称
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        //获取方法中的参数名称，parameterNames[]中的值与args[]中值是key-value关系，如果有全局参数控制，可以在这里对参数值进行修改
        String[] parameterNames = discoverer.getParameterNames(method);
        //执行程序，得到程序执行结果proceed
        Object proceed = pc.proceed(args);
        String encodedData ="";
        //将结果转成JSONString对象
        String s = JSON.toJSONString(proceed);
        if(!StringUtils.isEmpty(s)){
            //对结果进行非对称解密
            encodedData = RSAUtil.publicEncrypt(s, RSAUtil.getPublicKey(PUBLIC_KEY));
        }
        return encodedData;
    }
}
