package org.jdragon.springz.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author JDragon
 * @Date 2021.03.02 上午 1:06
 * @Email 1061917196@qq.com
 * @Des:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Destroy {
}
