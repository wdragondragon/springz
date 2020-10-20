package org.jdragon.springz.core;

import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.ComponentScan;
import org.jdragon.springz.core.annotation.ComponentScan.Filter;
import org.jdragon.springz.core.annotation.ComponentScans;
import org.jdragon.springz.core.entry.ClassInfo;
import org.jdragon.springz.core.entry.BasePackageInfo;
import org.jdragon.springz.core.entry.FilterInfo;
import org.jdragon.springz.core.scan.ScanAction;

import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.29 20:48
 * @Description: basePackage的容器，给扫描注册做准备
 */
public class BaseClassesScanContext implements ScanAction {

    private final Map<String,BasePackageInfo> basePackageInfoMap = new HashMap<>();

    @Override
    public void action(ClassInfo classInfo) {
        //TODO 根据ClassInfo获取ComponentScan注解并生成componentScanInfo
        Class<?> clazz = classInfo.getClazz();

        if(clazz.isAnnotationPresent(ComponentScans.class)){
            for(ComponentScan componentScan:clazz.getAnnotation(ComponentScans.class).value()){
                resolverComponentScan(componentScan);
            }
        }else if(clazz.isAnnotationPresent(ComponentScan.class)){
            resolverComponentScan(clazz.getAnnotation(ComponentScan.class));
        }

    }
    private void resolverComponentScan(ComponentScan componentScan){
        Class<?>[] basePackageClasses = componentScan.basePackageClasses();
        String[] basePackages = componentScan.basePackage();
        boolean useDefaultFilters = componentScan.useDefaultFilters();
        Filter[] excludeFilters = componentScan.excludeFilters();
        Filter[] includeFilters = componentScan.includeFilters();

        FilterInfo[] includeFiltersInfo = new FilterInfo[includeFilters.length];
        FilterInfo[] excludeFiltersInfo = new FilterInfo[excludeFilters.length];

        int i = 0;
        for(Filter filter:includeFilters){
            includeFiltersInfo[i] = new FilterInfo(filter.type(),filter.classes());
            i++;
        }
        i = 0;
        for(Filter filter:excludeFilters){
            excludeFiltersInfo[i] = new FilterInfo(filter.type(),filter.classes());
            i++;
        }

        BasePackageInfo basePackageInfo =
                new BasePackageInfo(useDefaultFilters,includeFiltersInfo,excludeFiltersInfo);
        for(Class<?> basePackageClass:basePackageClasses){
            String basePackage = basePackageClass.getPackage().getName();
            basePackageInfoMap.put(basePackage,basePackageInfo);
        }
        for(String basePackage:basePackages){
            basePackageInfoMap.put(basePackage,basePackageInfo);
        }
    }

    public Map<String, BasePackageInfo> getBasePackageInfoMap(){
        return basePackageInfoMap;
    }

    public String[] getBasePackages(String baseClassesName[]){
        Set<String> basePackageSet =  basePackageInfoMap.keySet();
        String[] basePackages = new String[basePackageSet.size()];
        if(basePackageSet.size()==0){
            basePackageInfoMap.put(baseClassesName[0],new BasePackageInfo());
            return baseClassesName;
        }
        int i = 0;
        for(String basePackage:basePackageSet){
            basePackages[i] = basePackage;
            i++;
        }
        return basePackages;
    }
}
