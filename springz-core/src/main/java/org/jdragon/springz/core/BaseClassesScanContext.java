package org.jdragon.springz.core;


import org.jdragon.springz.core.annotation.SpringzScan;
import org.jdragon.springz.core.annotation.SpringzScan.ComponentFilter;
import org.jdragon.springz.core.annotation.SpringzScans;
import org.jdragon.springz.core.entry.FilterInfo;
import org.jdragon.springz.core.scan.BasePackageInfo;

import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.ClassInfo;


import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.29 20:48
 * @Description: basePackage的容器，给扫描注册做准备
 */
public class BaseClassesScanContext implements ScanAction {

    private final Map<String, BasePackageInfo> basePackageInfoMap = new HashMap<>();

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

        FilterInfo[] includeFiltersInfo = new FilterInfo[includeComponentFilters.length];
        FilterInfo[] excludeFiltersInfo = new FilterInfo[excludeComponentFilters.length];

        int i = 0;
        for (ComponentFilter componentFilter : includeComponentFilters) {
            includeFiltersInfo[i] = new FilterInfo(componentFilter.type(), componentFilter.classes());
            i++;
        }
        i = 0;
        for (ComponentFilter componentFilter : excludeComponentFilters) {
            excludeFiltersInfo[i] = new FilterInfo(componentFilter.type(), componentFilter.classes());
            i++;
        }

        BasePackageInfo basePackageInfo =
                new BasePackageInfo(useDefaultFilters, includeFiltersInfo, excludeFiltersInfo);
        for (Class<?> basePackageClass : basePackageClasses) {
            String basePackage = basePackageClass.getPackage().getName();
            basePackageInfoMap.put(basePackage, basePackageInfo);
        }
        for (String basePackage : basePackages) {
            basePackageInfoMap.put(basePackage, basePackageInfo);
        }
    }

    public Map<String, BasePackageInfo> getBasePackageInfoMap() {
        return basePackageInfoMap;
    }

    public String[] getBasePackages(String[] baseClassesName) {
        Set<String> basePackageSet = basePackageInfoMap.keySet();
        String[] basePackages = new String[basePackageSet.size()];
        if (basePackageSet.size() == 0) {
            basePackageInfoMap.put(baseClassesName[0], new BasePackageInfo());
            return baseClassesName;
        }
        int i = 0;
        for (String basePackage : basePackageSet) {
            basePackages[i] = basePackage;
            i++;
        }

        return basePackages;
    }
}
