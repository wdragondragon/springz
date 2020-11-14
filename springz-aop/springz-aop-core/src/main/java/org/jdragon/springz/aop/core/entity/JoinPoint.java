package org.jdragon.springz.aop.core.entity;

import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 01:08
 * @Description:
 */
public class JoinPoint {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //执行目标对象
    private final Object targetObject;
    //代理对象
    private final Object proxy;
    //目标执行方法
    private final Method targetMethod;
    //目标方法执行参数
    private final Object[] args;
    //目标方法执行结果
    private Object invokeResult;

    public Object proceed() {
        return MethodUtils.invoke(targetObject, targetMethod, args);
    }

    public Object proceed(Method beforeMethod, Method afterMethod, Method throwableMethod) {
        try {
            //执行方法前置处理
            if (beforeMethod != null) {
                beforeMethod.invoke(proxy, this);
            }
            Object invoke = targetMethod.invoke(targetObject, args);
            if (invoke != null) {
                invokeResult = invoke;
            }
            //执行方法后置处理
            if (afterMethod != null) {
                afterMethod.invoke(proxy, this);
            }
            return invoke;
        } catch (Throwable e) {
            logger.error("切面方法异常", targetObject.getClass().getName(), targetMethod.getName());
            logger.error("ErrorCause", e.getCause().toString());
            logger.error("ErrorMessage", e.getMessage());
            logger.error("ErrorStackTrace", Arrays.toString(e.getStackTrace()));
            try {
                //执行异常后置处理
                if (throwableMethod != null)
                    throwableMethod.invoke(proxy, e);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                logger.error("切面执行异常", e.toString());
                ex.printStackTrace();
            }
        }
        return null;
    }

    public JoinPoint(Object targetObject, Object proxy, Method targetMethod, Object[] args) {
        this.targetObject = targetObject;
        this.proxy = proxy;
        this.targetMethod = targetMethod;
        this.args = args;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getInvokeResult() {
        return invokeResult;
    }
}
