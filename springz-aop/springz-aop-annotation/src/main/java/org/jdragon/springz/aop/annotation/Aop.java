package org.jdragon.springz.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.29 09:48
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Aop {

    String value() default "";
}