package org.jdragon.springz.core.annotation;

import java.lang.annotation.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 22:04
 * @Description: 与@Autowired搭配使用，在使用@Qualifier时，@Autowired中会使用@Qualifier中的值来代替获取字段类型名。
 */
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Qualifier {
    String value() default "";
}
