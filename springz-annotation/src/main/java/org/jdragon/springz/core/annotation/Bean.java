package org.jdragon.springz.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 15:04
 * @Description:
 * 声明一个方法是bean工厂，在启动时，其注解value值为beanName，返回值为bean，
 * 加入容器中，若beanName值为空，则使用方法名，默认使用单例模式注入
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {

    String value() default "";

}
