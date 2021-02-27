package org.jdragon.springz.core.filter;


import org.jdragon.springz.core.container.BaseClassPackagesContainer;
import org.jdragon.springz.core.annotation.FilterType;
import org.jdragon.springz.core.entry.FilterMeta;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.core.entry.BasePackageInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.30 08:17
 * @Description: 扫描过滤器
 */
public class BaseFilter implements Filter {

    /**
     * 使用默认过滤器时需要允许通过的注解类型
     */
    private final List<String> useDefault = Arrays.asList("RestController","Controller", "Service", "Component", "Configuration", "Repository", "IocMain");

    public BaseFilter() {
    }

    /**
     * @Author: Jdragon
     * @Date: 2020.04.30 下午 5:25
     * @params: [classInfo]
     * @return: boolean
     * @Description: 是否同意扫描加载，代码乱，待重构
     **/
    public boolean isAgree(ClassInfo classInfo) {
        Map<String, BasePackageInfo> basePackageInfoMap = BaseClassPackagesContainer.getBasePackageInfoMap();

        String classPackage = classInfo.getClassName();
        Class<?> clazz = classInfo.getClazz();
        int maxLength = 0;
        String basePackageOnly = null;
        for (String basePackage : basePackageInfoMap.keySet()) {
            if (classPackage.indexOf(basePackage) == 0 && maxLength < basePackage.length()) {
                basePackageOnly = basePackage;
                maxLength = basePackage.length();
            }
        }
        if (basePackageOnly == null) {
            return false;
        }

        BasePackageInfo basePackageInfo = basePackageInfoMap.get(basePackageOnly);

        FilterMeta[] includeFiltersInfo = basePackageInfo.getIncludeFiltersInfo();
        FilterMeta[] excludeFiltersInfo = basePackageInfo.getExcludeFiltersInfo();

        boolean useDefaultFilters = basePackageInfo.isUseDefaultFilters();

        //先判断是否使用默认类型，如果符合默认过滤器，直接返true
        if (useDefaultFilters) {
            Annotation[] annotations = clazz.getAnnotations();
            for (Annotation annotation : annotations) {
                String annotationName = annotation.annotationType().getSimpleName();
                if (useDefault.contains(annotationName)) {
                    return true;
                }
            }
        }
        boolean exclude = judge(clazz, excludeFiltersInfo);
        boolean include = judge(clazz, includeFiltersInfo);
        //当exclude不成立并且include成立时通过
        return !exclude && include;
    }

    /**
     * @params: [clazz, filtersInfo]
     * @return: boolean
     * @Description: 判断exclude与include
     **/
    private boolean judge(Class<?> clazz, FilterMeta[] filtersInfo) {
        for (FilterMeta filterMeta : filtersInfo) {
            FilterType filterType = filterMeta.getType();
            Class<?>[] filterClasses = filterMeta.getClasses();

            if (filterType.equals(FilterType.ANNOTATION)) {
                for (Class<?> filterClass : filterClasses) {
                    String filterClassName = filterClass.getName();
                    Annotation[] classAnnotations = clazz.getAnnotations();
                    for (Annotation classAnnotation : classAnnotations) {
                        String classAnnotationName = classAnnotation.annotationType().getName();
                        if (classAnnotationName.equals(filterClassName)) {
                            return true;
                        }
                    }
                }
            } else if (filterType.equals(FilterType.ASSIGNABLE_TYPE)) {
                for (Class<?> filterClass : filterClasses) {
                    if (filterClass.equals(clazz)) {
                        return true;
                    }
                }
            } else if (filterType.equals(FilterType.CUSTOM)) {
                for (Class<?> filterClass : filterClasses) {
                    try {
                        Method match = filterClass.getDeclaredMethod("match", Class.class);
                        if ((boolean) match.invoke(filterClass.newInstance(), clazz)) {
                            return true;
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
