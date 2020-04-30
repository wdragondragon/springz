package org.jdragon.springz.test.component;

import org.jdragon.springz.core.scan.TypeFilter;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.30 16:49
 * @Description:
 */
public class MyFilter implements TypeFilter {
    public boolean match(Class<?> clazz) {
        System.out.println(clazz.getName());
        return false;
    }
}
