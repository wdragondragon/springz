package org.jdragon.springz.core.annotation;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:07
 * @Description: 属于@Component，声明一个类是组件，在扫描时加入容器
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Component
public @interface Controller {
    String value() default "";
}
