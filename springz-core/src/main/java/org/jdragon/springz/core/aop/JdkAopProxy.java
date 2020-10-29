package org.jdragon.springz.core.aop;

import org.jdragon.springz.core.entry.MethodInvocation;
import org.jdragon.springz.core.entry.PointCutInfo;
import org.jdragon.springz.core.utils.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkAopProxy implements InvocationHandler {

    private final Object target;

    private final PointCutInfo pointCutInfo;

    private Object proxy;

    private Method beforeMethod;

    private Method afterMethod;


    private JdkAopProxy(Object target, PointCutInfo pointCutInfo) {
        this.target = target;
        this.pointCutInfo = pointCutInfo;
        this.proxy = pointCutInfo.getProxyObj();
        this.beforeMethod = pointCutInfo.getBeforeMethod();
        this.afterMethod = pointCutInfo.getAfterMethod();
    }

    public static Object wrap(Object target, PointCutInfo pointCutInfo) {
        JdkAopProxy jdkAopProxy = new JdkAopProxy(target, pointCutInfo);
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), jdkAopProxy);
    }

    @Override
    public Object invoke(Object obj, Method method, Object[] args) {
        MethodInvocation methodInvocation = new MethodInvocation(target, proxy, method, args);
        String pointCutMethod = pointCutInfo.getPointCut().method();
        if (PatternMatchUtils.simpleMatch(pointCutMethod, method.getName())) {
            return methodInvocation.proceed(beforeMethod, afterMethod);
        } else {
            return methodInvocation.proceed();
        }
    }
}
