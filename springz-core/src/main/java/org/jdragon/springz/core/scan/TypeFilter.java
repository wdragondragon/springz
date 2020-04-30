package org.jdragon.springz.core.scan;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.30 16:59
 * @Description: 实现这个接口，使用自定义规则过滤
 */
public interface TypeFilter {
    boolean match(Class<?> clazz);
}
