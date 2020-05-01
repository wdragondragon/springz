package org.jdragon.springz.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.01 01:37
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Import {
    Class<?>[] value();
}
