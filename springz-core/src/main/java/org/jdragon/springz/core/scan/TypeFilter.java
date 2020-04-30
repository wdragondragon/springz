package org.jdragon.springz.core.scan;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.30 16:59
 * @Description:
 */
public interface TypeFilter {
    boolean match(Class<?> clazz);
}
