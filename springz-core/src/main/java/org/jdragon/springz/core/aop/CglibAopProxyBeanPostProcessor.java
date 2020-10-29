package org.jdragon.springz.core.aop;

import org.jdragon.springz.core.entry.PointCutInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 08:49
 * @Description:
 */
public class CglibAopProxyBeanPostProcessor extends AbstractAopProxyBeanPostProcessor {
    @Override
    public Object wrapBean(Object target, PointCutInfo pointCutInfo) {
        return CglibAopProxy.wrap(target, pointCutInfo);
    }
}
