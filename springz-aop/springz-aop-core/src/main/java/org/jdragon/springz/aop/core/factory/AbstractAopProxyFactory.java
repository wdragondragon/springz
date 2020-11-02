package org.jdragon.springz.aop.core.factory;

import org.jdragon.springz.aop.core.AopContext;
import org.jdragon.springz.aop.core.entity.PointCutInfo;
import org.jdragon.springz.scanner.entry.BeanInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 08:41
 * @Description:
 */
public abstract class AbstractAopProxyFactory {
    public Object getProxyBean(Object bean,String initClassName) {
        //链式包装目标类
        for (PointCutInfo pointCutInfo : AopContext.get(initClassName)) {
            bean = wrapBean(bean, pointCutInfo);
        }
        return bean;
    }

    public abstract Object wrapBean(Object target, PointCutInfo pointCutInfo);
}
