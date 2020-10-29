package org.jdragon.springz.core.entry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 01:08
 * @Description:
 */
public class MethodInvocation {
    //target object
    private final Object targetObject;

    private final Object proxy;
    //target method
    private final Method targetMethod;
    //the parameter of target method
    private final Object[] args;

    public Object proceed() {
        try {
            return targetMethod.invoke(targetObject, args);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
        return null;
    }

    public Object proceed(Method beforeMethod, Method afterMethod) {
        try {
            if (beforeMethod != null) {
                beforeMethod.invoke(proxy, this);
            }
            Object invoke = targetMethod.invoke(targetObject, args);

            if (afterMethod != null) {
                afterMethod.invoke(proxy, invoke, this);
            }
            return invoke;
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
        return null;
    }

    public MethodInvocation(Object targetObject, Object proxy, Method targetMethod, Object[] args) {
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
}
