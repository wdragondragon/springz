package org.jdragon.springz.core.container;


import org.jdragon.springz.core.entry.PostAutowiredBean;
import org.jdragon.springz.core.processor.BeanPostProcessor;
import org.jdragon.springz.core.processor.PropertyPostProcessor;
import org.jdragon.springz.core.processor.TestPostProcessor;
import org.jdragon.springz.scanner.entry.BeanInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 11:13
 * @Description: 后置处理器容器
 */
public class PostProcessorContainer {
    private static final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    static {
        PostProcessorContainer.registerBeanPostProcessor(new TestPostProcessor());
        PostProcessorContainer.registerBeanPostProcessor(new PropertyPostProcessor());
    }

    public static List<BeanPostProcessor> get() {
        //根据order优先级来排序注册先后
        beanPostProcessors.sort(Comparator.comparing(BeanPostProcessor::getOrder));
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
