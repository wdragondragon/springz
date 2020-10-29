package org.jdragon.springz.core.aop;

import org.jdragon.springz.core.entry.PointCutInfo;
import org.jdragon.springz.core.utils.AopContext;
import org.jdragon.springz.scanner.entry.BeanInfo;

import java.lang.reflect.Method;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 08:41
 * @Description:
 */
public abstract class AbstractAopProxyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean) {
        BeanInfo beanInfo = (BeanInfo) bean;
        Object wrapperProxyBean = beanInfo.getBean();
        //链式包装目标类
        for (PointCutInfo pointCutInfo : AopContext.get(beanInfo.getClassName())) {
            wrapperProxyBean = wrapBean(wrapperProxyBean, pointCutInfo);
        }
        return wrapperProxyBean;
    }

    public abstract Object wrapBean(Object target, PointCutInfo pointCutInfo);
}
