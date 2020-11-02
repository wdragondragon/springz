package org.jdragon.springz.aop.core.entity;

import org.jdragon.springz.aop.annotation.Pointcut;

import java.lang.reflect.Method;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 00:14
 * @Description:
 */
public class PointCutInfo {

    private Object proxyObj;

    private Pointcut pointCut;

    private Method beforeMethod;

    private Method afterMethod;

    private Method throwableMethod;

    private int order;

    public PointCutInfo(Object proxyObj, Pointcut pointCut, Method beforeMethod, Method afterMethod,Method throwableMethod, int order) {
        this.proxyObj = proxyObj;
        this.pointCut = pointCut;
        this.afterMethod = afterMethod;
        this.beforeMethod = beforeMethod;
        this.throwableMethod = throwableMethod;
        this.order = order;
    }

    public Pointcut getPointCut() {
        return pointCut;
    }

    public int getOrder() {
        return order;
    }

    public Object getProxyObj() {
        return proxyObj;
    }

    public Method getBeforeMethod() {
        return beforeMethod;
    }

    public Method getAfterMethod() {
        return afterMethod;
    }

    public Method getThrowableMethod() {
        return throwableMethod;
    }
}
