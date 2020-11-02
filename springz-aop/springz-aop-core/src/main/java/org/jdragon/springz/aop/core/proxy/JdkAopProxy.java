package org.jdragon.springz.aop.core.proxy;

import org.jdragon.springz.aop.core.entity.JoinPoint;
import org.jdragon.springz.aop.core.entity.PointCutInfo;
import org.jdragon.springz.aop.core.utils.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkAopProxy implements InvocationHandler {

    private final Object target;

    private final PointCutInfo pointCutInfo;

    private final Object proxy;

    private final Method beforeMethod;

    private final Method afterMethod;

    private final Method throwableMethod;

    private JdkAopProxy(Object target, PointCutInfo pointCutInfo) {
        this.target = target;
        this.pointCutInfo = pointCutInfo;
        this.proxy = pointCutInfo.getProxyObj();
        this.beforeMethod = pointCutInfo.getBeforeMethod();
        this.afterMethod = pointCutInfo.getAfterMethod();
        this.throwableMethod = pointCutInfo.getThrowableMethod();
    }

    public static Object wrap(Object target, PointCutInfo pointCutInfo) {
        JdkAopProxy jdkAopProxy = new JdkAopProxy(target, pointCutInfo);
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), jdkAopProxy);
    }

    @Override
    public Object invoke(Object obj, Method method, Object[] args) {
        JoinPoint joinPoint = new JoinPoint(target, proxy, method, args);
        String pointCutMethod = pointCutInfo.getPointCut().method();
        if (PatternMatchUtils.simpleMatch(pointCutMethod, method.getName())) {
            return joinPoint.proceed(beforeMethod, afterMethod, throwableMethod);
        } else {
            return joinPoint.proceed();
        }
    }
}
