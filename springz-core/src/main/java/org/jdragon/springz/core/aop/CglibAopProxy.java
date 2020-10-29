package org.jdragon.springz.core.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.jdragon.springz.core.entry.MethodInvocation;
import org.jdragon.springz.core.entry.PointCutInfo;
import org.jdragon.springz.core.utils.PatternMatchUtils;

import java.lang.reflect.Method;

/**
 * a
 *
 * @author shuang.kou & tom
 * @createTime 2020年10月09日 21:43:00
 **/
public class CglibAopProxy implements MethodInterceptor {

    private final Object target;

    private final PointCutInfo pointCutInfo;

    private Object proxy;

    private Method beforeMethod;

    private Method afterMethod;

    public CglibAopProxy(Object target, PointCutInfo pointCutInfo) {
        this.target = target;
        this.pointCutInfo = pointCutInfo;
        this.proxy = pointCutInfo.getProxyObj();
        this.beforeMethod = pointCutInfo.getBeforeMethod();
        this.afterMethod = pointCutInfo.getAfterMethod();
    }

    public static Object wrap(Object target, PointCutInfo pointCutInfo) {
        Class<?> rootClass = target.getClass();
        Class<?> proxySuperClass = rootClass;
        // cglib 多级代理处理
        if (target.getClass().getName().contains("$$")) {
            proxySuperClass = rootClass.getSuperclass();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(target.getClass().getClassLoader());
        enhancer.setSuperclass(proxySuperClass);
        enhancer.setCallback(new CglibAopProxy(target, pointCutInfo));
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) {
        MethodInvocation methodInvocation = new MethodInvocation(target, proxy, method, args);
        // the return value is still the result of the proxy class execution
        String pointCutMethod = pointCutInfo.getPointCut().method();
        if(PatternMatchUtils.simpleMatch(pointCutMethod,method.getName())){
            return methodInvocation.proceed(beforeMethod, afterMethod);
        }else{
            return methodInvocation.proceed();
        }
    }
}
