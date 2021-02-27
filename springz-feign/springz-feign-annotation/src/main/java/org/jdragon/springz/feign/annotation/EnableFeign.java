package org.jdragon.springz.feign.annotation;

import org.jdragon.springz.core.annotation.IocScan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.22 13:13
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@IocScan(basePackage = "org.jdragon.springz.feign.core")
public @interface EnableFeign {
    
}
