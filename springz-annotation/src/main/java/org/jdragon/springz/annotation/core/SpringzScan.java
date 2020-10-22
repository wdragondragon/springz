package org.jdragon.springz.annotation.core;

import java.lang.annotation.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 19:12
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(SpringzScans.class)
public @interface SpringzScan {
    /**
     * 包名扫描
     */
    String[] basePackage() default {};

    /**
     * 类名扫描
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * 是否使用默认的拥有Component就加入容器中
     */
    boolean useDefaultFilters() default true;

    /**
     * 包含某些条件的组件
     */
    ComponentFilter[] includeFilters() default {};

    /**
     * 不包括某些条件的组件
     */
    ComponentFilter[] excludeFilters() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @Target({})
    @interface ComponentFilter {

        Class<?>[] value() default {};

        FilterType type() default FilterType.ANNOTATION;

        Class<?>[] classes() default {};

    }
}
