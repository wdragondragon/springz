package org.jdragon.springz.core.processor;

import org.jdragon.springz.core.entry.PostAutowiredBean;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 08:40
 * @Description: 后置处理器接口
 */
public interface BeanPostProcessor {
    default PostAutowiredBean postProcessAfterInitialization(PostAutowiredBean postAutowiredBean) {
        return postAutowiredBean;
    }
}