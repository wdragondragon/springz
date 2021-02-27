package org.jdragon.springz.aop.annotation;

import org.jdragon.springz.core.annotation.IocScan;

import java.lang.annotation.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 11:53
 * @Description:
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@IocScan(basePackage = "org.jdragon.springz.aop.core")
public @interface EnableAop {
}
