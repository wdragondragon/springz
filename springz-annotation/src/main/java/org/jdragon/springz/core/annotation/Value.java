package org.jdragon.springz.core.annotation;

import java.lang.annotation.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 00:34
 * @Description: 给组件中的字段赋予默认值，只能赋予有String.class类型的构造的值
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
    String value();
}
