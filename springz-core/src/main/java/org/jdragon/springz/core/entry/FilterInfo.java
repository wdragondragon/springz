package org.jdragon.springz.core.entry;

import org.jdragon.springz.annotation.core.FilterType;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.29 21:10
 * @Description:
 */
public class FilterInfo {

    private FilterType type;

    private Class<?>[] classes;

    public FilterInfo() {
    }

    public FilterInfo(FilterType type, Class<?>[] classes) {
        this.type = type;
        this.classes = classes;
    }


    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public Class<?>[] getClasses() {
        return classes;
    }

    public void setClasses(Class<?>[] classes) {
        this.classes = classes;
    }
}
