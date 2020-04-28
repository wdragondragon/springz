package org.jdragon.springz.core.annotation;

import org.jdragon.springz.core.entry.FilterType;

import java.lang.annotation.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 19:12
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ComponentScans.class)
public @interface ComponentScan {
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
    Filter[] includeFilters() default {};

    /**
     * 不包括某些条件的组件
     */
    Filter[] excludeFilters() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @Target({})
    @interface Filter {

        Class<?>[] value() default {};

        FilterType type() default FilterType.ANNOTATION;

        Class<?>[] classes() default {};

        String[] pattern() default {};
    }
}
