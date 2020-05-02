package org.jdragon.springz.core;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 15:12
 * @Description: 注解注册行为接口
 */
public interface AnnotationRegister {
    /**
     * 扫描并注册Component与bean组件
     */
    void registerType();

    /**
     * 注入bean
     */
    void injection();

}
