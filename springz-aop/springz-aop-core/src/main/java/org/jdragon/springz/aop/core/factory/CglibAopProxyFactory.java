package org.jdragon.springz.aop.core.factory;

import org.jdragon.springz.aop.core.proxy.CglibAopProxy;
import org.jdragon.springz.aop.core.entity.PointCutInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 08:49
 * @Description:
 */
public class CglibAopProxyFactory extends AbstractAopProxyFactory {
    @Override
    public Object wrapBean(Object target, PointCutInfo pointCutInfo) {
        return CglibAopProxy.wrap(target, pointCutInfo);
    }
}
