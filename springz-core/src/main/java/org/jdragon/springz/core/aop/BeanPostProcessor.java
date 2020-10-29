package org.jdragon.springz.core.aop;

import org.jdragon.springz.scanner.entry.BeanInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 08:40
 * @Description:
 */
public interface BeanPostProcessor {
    default Object postProcessAfterInitialization(Object bean) {
        return bean;
    }
}