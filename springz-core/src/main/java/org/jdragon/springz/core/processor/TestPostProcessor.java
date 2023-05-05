package org.jdragon.springz.core.processor;

import org.jdragon.springz.core.entry.PostAutowiredBean;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 11:39
 * @Description: 用于处理多例的bean后置处理器
 */
public class TestPostProcessor implements BeanPostProcessor {
    @Override
    public PostAutowiredBean postProcessAfterInitialization(PostAutowiredBean postAutowiredBean) {
//        System.out.println("postProcessAfterInitialization");
        return postAutowiredBean;
    }
}