package org.jdragon.springz.core.entry;

import org.jdragon.springz.core.filter.FilterMeta;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.29 22:06
 * @Description: 用于放止扫描路径集合
 */
public class BasePackageInfo {

    private boolean useDefaultFilters;

    private FilterMeta[] includeFiltersInfo;

    private FilterMeta[] excludeFiltersInfo;

    public BasePackageInfo() {
        this.useDefaultFilters = true;
        this.includeFiltersInfo = new FilterMeta[]{};
        this.excludeFiltersInfo = new FilterMeta[]{};
    }

    public BasePackageInfo(boolean useDefaultFilters, FilterMeta[] includeFiltersInfo, FilterMeta[] excludeFiltersInfo) {
        this.useDefaultFilters = useDefaultFilters;
        this.includeFiltersInfo = includeFiltersInfo;
        this.excludeFiltersInfo = excludeFiltersInfo;
    }

    public boolean isUseDefaultFilters() {
        return useDefaultFilters;
    }

    public void setUseDefaultFilters(boolean useDefaultFilters) {
        this.useDefaultFilters = useDefaultFilters;
    }

    public FilterMeta[] getIncludeFiltersInfo() {
        return includeFiltersInfo;
    }

    public void setIncludeFiltersInfo(FilterMeta[] includeFiltersInfo) {
        this.includeFiltersInfo = includeFiltersInfo;
    }

    public FilterMeta[] getExcludeFiltersInfo() {
        return excludeFiltersInfo;
    }

    public void setExcludeFiltersInfo(FilterMeta[] excludeFiltersInfo) {
        this.excludeFiltersInfo = excludeFiltersInfo;
    }
}
