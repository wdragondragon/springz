package org.jdragon.springz.aop.core.factory;

import org.jdragon.springz.aop.core.proxy.JdkAopProxy;
import org.jdragon.springz.aop.core.entity.PointCutInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 08:41
 * @Description:
 */
public class JdkAopProxyFactory extends AbstractAopProxyFactory {
    @Override
    public Object wrapBean(Object target, PointCutInfo pointCutInfo) {
        return JdkAopProxy.wrap(target, pointCutInfo);
    }
}
