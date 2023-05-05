package org.jdragon.springz.feign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.21 18:39
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZFeign {

    String baseUrl();

    String basePath() default "";

    /**
     * @Description: 解构提取返回结果
    **/

    /**
     * @Description: 解构提取返回结果
     **/
    String depth() default "";

    String[] headers() default {};

    String fallback() default "";
}
