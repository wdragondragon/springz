package org.jdragon.springz.core.filter;

import org.jdragon.springz.core.annotation.FilterType;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.29 21:10
 * @Description: 用于放置扫描拦截元数据
 */
public class FilterMeta {

    private FilterType type;

    private Class<?>[] classes;

    public FilterMeta() {
    }

    public FilterMeta(FilterType type, Class<?>[] classes) {
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
