package com.cmnt.dbpick.common.aspect;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description 描述
 * @date 2023-03-27 21:52
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmnt.dbpick.common.annotation.SysLog;
import com.cmnt.dbpick.common.base.BaseController;
import com.cmnt.dbpick.common.enums.StatusEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.model.HttpServerInfo;
import com.cmnt.dbpick.common.model.SysOperLog;
import com.cmnt.dbpick.common.mq.RocketMQProducer;
import com.cmnt.dbpick.common.mq.constant.RocketMQConstant;
import com.cmnt.dbpick.common.utils.ObjectTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LogAspect extends BaseController {

    @Autowired
    private RocketMQProducer rocketMQProducer;

    /**
     * @annotation(SysLog类的路径) 在idea中，右键自定义的SysLog类-> 点击Copy Reference
     */
    @Pointcut("@annotation(com.cmnt.dbpick.common.annotation.SysLog)")
    public void logPointCut() {
        log.info("------>配置织入点");
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e);
    }

    private void handleLog(final JoinPoint joinPoint, final Exception e) {

        // 获得SysLog注解
        SysLog controllerLog = getAnnotationLog(joinPoint);
        if (controllerLog == null) {
            return;
        }
        SysOperLog operLog = new SysOperLog();
        // 操作状态（0正常 1异常）
        operLog.setStatus(StatusEnum.SUCCESS.getCode());
        // 操作时间
        operLog.setOperTime(new Date());
        if (ObjectTools.isNotNull(e)) {
            operLog.setStatus(StatusEnum.FAILED.getCode());
            // BizException为本系统自定义的异常类，读者若要获取异常信息，请根据自身情况变通
            operLog.setErrorMsg(StringUtils.substring(((BizException) e).getMessage(), 0, 2000));
        }

        HttpServerInfo httpServerInfo = getServerInfo(getRequest());
        operLog.setHttpServerInfo(httpServerInfo);
        operLog.setUri(httpServerInfo.getUri());
        operLog.setIp(getIpAddress(getRequest()));
        // 处理注解上的参数
        getControllerMethodDescription(joinPoint, controllerLog, operLog);
        rocketMQProducer.sendAsyncMsg(RocketMQConstant.CONFIG_SYSLOG_AOP_TOPIT,
                JSON.toJSONString(operLog));
        log.info("----------------------发送成功MQ-----------------------------");
    }

    /**
     * 是否存在注解，如果存在就获取，不存在则返回null
     * @param joinPoint
     * @return
     */
    private SysLog getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (ObjectTools.isNotNull(method)) {
            return method.getAnnotation(SysLog.class);
        }
        return null;
    }

    /**
     * 获取Controller层上SysLog注解中对方法的描述信息
     * @param joinPoint 切点
     * @param sysLog 自定义的注解
     * @param operLog 操作日志实体类
     */
    private void getControllerMethodDescription(JoinPoint joinPoint, SysLog sysLog, SysOperLog operLog) {
        // 设置业务类型（0其它 1新增 2修改 3删除）
        operLog.setBusinessType(sysLog.businessType().ordinal());
        // 设置模块标题，eg:登录
        operLog.setTitle(sysLog.title());
        // 对方法上的参数进行处理，处理完：userName=xxx,password=xxx
        String optParam = getAnnotationValue(joinPoint, sysLog.optParam());
        operLog.setOptParam(optParam);

    }

    /**
     * 对方法上的参数进行处理
     * @param joinPoint
     * @param name
     * @return
     */
    private String getAnnotationValue(JoinPoint joinPoint, String name) {
        String paramName = name;
        // 获取方法中所有的参数
        Map<String, Object> params = getParams(joinPoint);
        // 参数是否是动态的:#{paramName}
        if (paramName.matches("^#\\{\\D*\\}")) {
            // 获取参数名,去掉#{ }
            paramName = paramName.replace("#{", "").replace("}", "");
            // 是否是复杂的参数类型:对象.参数名
            if (paramName.contains(".")) {
                String[] split = paramName.split("\\.");
                // 获取方法中对象的内容
                Object object = getValue(params, split[0]);
                // 转换为JsonObject
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);
                // 获取值
                Object o = jsonObject.get(split[1]);
                return String.valueOf(o);
            } else {// 简单的动态参数直接返回
                StringBuilder str = new StringBuilder();
                String[] paraNames = paramName.split(",");
                for (String paraName : paraNames) {

                    String val = String.valueOf(getValue(params, paraName));
                    // 组装成 userName=xxx,password=xxx,
                    str.append(paraName).append("=").append(val).append(",");
                }
                // 去掉末尾的,
                if (str.toString().endsWith(",")) {
                    String substring = str.substring(0, str.length() - 1);
                    return substring;
                } else {
                    return str.toString();
                }
            }
        }
        // 非动态参数直接返回
        return name;
    }

    /**
     * 获取方法上的所有参数，返回Map类型, eg: 键："userName",值:xxx  键："password",值:xxx
     * @param joinPoint
     * @return
     */
    public Map<String, Object> getParams(JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>(8);
        // 通过切点获取方法所有参数值["zhangsan", "123456"]
        Object[] args = joinPoint.getArgs();
        // 通过切点获取方法所有参数名 eg:["userName", "password"]
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] names = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            params.put(names[i], args[i]);
        }
        return params;
    }

    /**
     * 从map中获取键为paramName的值，不存在放回null
     * @param map
     * @param paramName
     * @return
     */
    private Object getValue(Map<String, Object> map, String paramName) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals(paramName)) {
                return entry.getValue();
            }
        }
        return null;
    }
}

