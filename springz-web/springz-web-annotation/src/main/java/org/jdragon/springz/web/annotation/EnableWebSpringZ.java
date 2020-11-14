package org.jdragon.springz.web.annotation;

import org.jdragon.springz.core.annotation.SpringzScan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.27 20:19
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringzScan(basePackage = "org.jdragon.springz.web.core")
public @interface EnableWebSpringZ {
}
