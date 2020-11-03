package org.jdragon.springz.aop.core.factory;

import org.jdragon.springz.core.entry.PostAutowiredBean;
import org.jdragon.springz.core.processor.BeanPostProcessor;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 01:25
 * @Description:
 */
public class AopProxyPostProcessorFactory implements BeanPostProcessor {
    /**
     * @param beanClass 目标类
     * @return beanClass 实现了接口就返回jdk动态代理bean后置处理器,否则返回 cglib动态代理bean后置处理器
     */
    public AbstractAopProxyFactory getAopProxy(Class<?> beanClass) {
        if (beanClass.isInterface() || beanClass.getInterfaces().length > 0) {
            return new JdkAopProxyFactory();
        } else {
            return new CglibAopProxyFactory();
        }
    }

    @Override
    public PostAutowiredBean postProcessAfterInitialization(PostAutowiredBean postAutowiredBean) {
        Class<?> beanClass = postAutowiredBean.getBeanInfo().getBean().getClass();
        AbstractAopProxyFactory abstractAopProxyFactory = this.getAopProxy(beanClass);
        Object lastBean = abstractAopProxyFactory.getProxyBean(
                postAutowiredBean.getLastBean(),
                postAutowiredBean.getBeanInfo().getClassName());
        postAutowiredBean.setLastBean(lastBean);
        return postAutowiredBean;
    }
}
