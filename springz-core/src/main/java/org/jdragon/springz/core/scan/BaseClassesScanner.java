package org.jdragon.springz.core.scan;


import org.jdragon.springz.core.BaseClassPackagesManager;
import org.jdragon.springz.core.annotation.SpringzScan;
import org.jdragon.springz.core.annotation.SpringzScan.ComponentFilter;
import org.jdragon.springz.core.annotation.SpringzScans;
import org.jdragon.springz.core.filter.FilterMeta;
import org.jdragon.springz.core.entry.BasePackageInfo;

import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.ClassInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.29 20:48
 * @Description: basePackage的容器，给扫描注册做准备
 */
public class BaseClassesScanner implements ScanAction {

    @Override
    public void action(ClassInfo classInfo) {
        //TODO 根据ClassInfo获取ComponentScan注解并生成componentScanInfo
        Class<?> clazz = classInfo.getClazz();
        if (clazz.isAnnotationPresent(SpringzScans.class)) {
            for (SpringzScan springzScan : clazz.getAnnotation(SpringzScans.class).value()) {
                resolverComponentScan(springzScan);
            }
        } else if (clazz.isAnnotationPresent(SpringzScan.class)) {
            resolverComponentScan(clazz.getAnnotation(SpringzScan.class));
        }
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }

    public void resolverComponentScan(SpringzScan springzScan) {
        Class<?>[] basePackageClasses = springzScan.basePackageClasses();
        String[] basePackages = springzScan.basePackage();
        boolean useDefaultFilters = springzScan.useDefaultFilters();
        ComponentFilter[] excludeComponentFilters = springzScan.excludeFilters();
        ComponentFilter[] includeComponentFilters = springzScan.includeFilters();

        FilterMeta[] includeFiltersInfo = new FilterMeta[includeComponentFilters.length];
        FilterMeta[] excludeFiltersInfo = new FilterMeta[excludeComponentFilters.length];

        int i = 0;
        for (ComponentFilter componentFilter : includeComponentFilters) {
            includeFiltersInfo[i] = new FilterMeta(componentFilter.type(), componentFilter.classes());
            i++;
        }
        i = 0;
        for (ComponentFilter componentFilter : excludeComponentFilters) {
            excludeFiltersInfo[i] = new FilterMeta(componentFilter.type(), componentFilter.classes());
            i++;
        }

        BasePackageInfo basePackageInfo =
                new BasePackageInfo(useDefaultFilters, includeFiltersInfo, excludeFiltersInfo);
        for (Class<?> basePackageClass : basePackageClasses) {
            String basePackage = basePackageClass.getPackage().getName();
            BaseClassPackagesManager.register(basePackage,basePackageInfo);
        }
        for (String basePackage : basePackages) {
            BaseClassPackagesManager.register(basePackage,basePackageInfo);
        }
    }
}
