package org.jdragon.springz.core.entry;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.29 22:06
 * @Description:
 */
public class BasePackageInfo {

    private boolean useDefaultFilters;

    private FilterInfo[] includeFiltersInfo;

    private FilterInfo[] excludeFiltersInfo;

    public BasePackageInfo() {
        this.useDefaultFilters = true;
        this.includeFiltersInfo = new FilterInfo[]{};
        this.excludeFiltersInfo = new FilterInfo[]{};
    }

    public BasePackageInfo(boolean useDefaultFilters, FilterInfo[] includeFiltersInfo, FilterInfo[] excludeFiltersInfo) {
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

    public FilterInfo[] getIncludeFiltersInfo() {
        return includeFiltersInfo;
    }

    public void setIncludeFiltersInfo(FilterInfo[] includeFiltersInfo) {
        this.includeFiltersInfo = includeFiltersInfo;
    }

    public FilterInfo[] getExcludeFiltersInfo() {
        return excludeFiltersInfo;
    }

    public void setExcludeFiltersInfo(FilterInfo[] excludeFiltersInfo) {
        this.excludeFiltersInfo = excludeFiltersInfo;
    }
}
