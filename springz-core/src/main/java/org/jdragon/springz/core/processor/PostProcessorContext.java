package org.jdragon.springz.core.processor;


import org.jdragon.springz.core.entry.PostAutowiredBean;
import org.jdragon.springz.scanner.entry.BeanInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 11:13
 * @Description: 后置处理器容器
 */
public class PostProcessorContext {
    private static final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    static {
        PostProcessorContext.registerBeanPostProcessor(new TestPostProcessor());
    }

    public static List<BeanPostProcessor> get() {
        return beanPostProcessors;
    }

    public static void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.add(beanPostProcessor);
    }

    public static Object invokePostProcessor(BeanInfo beanInfo, Object lastBean) {
        PostAutowiredBean postAutowiredBean = new PostAutowiredBean(beanInfo, lastBean);

        for (BeanPostProcessor beanPostProcessor : get()) {
            postAutowiredBean = beanPostProcessor.postProcessAfterInitialization(postAutowiredBean);
        }
        return postAutowiredBean.getLastBean();
    }

    public static Object invokePostProcessor(BeanInfo beanInfo) {
        return invokePostProcessor(beanInfo, beanInfo.getBean());
    }
}
