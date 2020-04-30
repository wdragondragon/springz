package org.jdragon.springz.core.scan;

import org.jdragon.springz.core.entry.BasePackageInfo;
import org.jdragon.springz.core.entry.ClassInfo;
import org.jdragon.springz.core.entry.FilterInfo;
import org.jdragon.springz.core.entry.FilterType;

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
public class Filter {

    private Map<String, BasePackageInfo> basePackageInfoMap;

    /**
     * 使用默认过滤器时需要允许通过的注解类型
     */
    private List<String> useDefault = Arrays.asList("Controller", "Service", "Component", "Configuration", "Repository", "SpringzMain");

    public Filter(Map<String, BasePackageInfo> basePackageInfoMap) {
        this.basePackageInfoMap = basePackageInfoMap;
    }
    /**
     * @Author: Jdragon
     * @Date: 2020.04.30 下午 5:25
     * @params: [classInfo]
     * @return: boolean
     * @Description: 是否同意扫描加载，代码乱，待重构
     **/
    public boolean isAgree(ClassInfo classInfo) {

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

        FilterInfo[] includeFiltersInfo = basePackageInfo.getIncludeFiltersInfo();
        FilterInfo[] excludeFiltersInfo = basePackageInfo.getExcludeFiltersInfo();

        boolean useDefaultFilters = basePackageInfo.isUseDefaultFilters();

        /**
            先判断是否使用默认类型，如果符合默认过滤器，直接返true
         */
        if (useDefaultFilters) {
            Annotation[] annotations = clazz.getAnnotations();
            for (Annotation annotation : annotations) {
                String annotationName = annotation.annotationType().getSimpleName();
                if (useDefault.contains(annotationName)) {
                    return true;
                }
            }
        }
        boolean exclude = false;
        boolean include = false;
        //根据exclude或include分开来写for循环，for循环中又有根据type过滤类型来进行的的判断
        for (FilterInfo excludeFilterInfo : excludeFiltersInfo) {
            FilterType filterType = excludeFilterInfo.getType();
            Class<?>[] excludeClasses = excludeFilterInfo.getClasses();
            if (filterType.equals(FilterType.ANNOTATION)) {
                for (Class<?> excludeClass : excludeClasses) {
                    String excludeClassName = excludeClass.getName();
                    Annotation[] classAnnotations = clazz.getAnnotations();
                    for (Annotation classAnnotation : classAnnotations) {
                        String classAnnotationName = classAnnotation.annotationType().getName();
                        if (classAnnotationName.equals(excludeClassName)) {
                            exclude = true;
                        }
                    }
                    if (exclude) {
                        break;
                    }
                }
            } else if (filterType.equals(FilterType.ASSIGNABLE_TYPE)) {
                for (Class<?> excludeClass : excludeClasses) {
                    if (excludeClass.equals(clazz)) {
                        exclude = true;
                        break;
                    }
                }
            } else if (filterType.equals(FilterType.CUSTOM)) {
                for (Class<?> excludeClass : excludeClasses) {
                    try {
                        Method match = excludeClass.getDeclaredMethod("match",Class.class);
                        boolean isRefuse = (boolean) match.invoke(excludeClass,clazz);
                        if (isRefuse) {
                            exclude = true;
                            break;
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (FilterInfo includeFilterInfo : includeFiltersInfo) {
            FilterType filterType = includeFilterInfo.getType();
            Class<?>[] includeClasses = includeFilterInfo.getClasses();
            if (filterType.equals(FilterType.ANNOTATION)) {
                for (Class<?> includeClass : includeClasses) {
                    String includeClassName = includeClass.getName();
                    Annotation[] classAnnotations = clazz.getAnnotations();
                    for (Annotation classAnnotation : classAnnotations) {
                        if (classAnnotation.annotationType().getName().equals(includeClassName)) {
                            include = true;
                        }
                    }
                }
            } else if (filterType.equals(FilterType.ASSIGNABLE_TYPE)) {
                for (Class<?> includeClass : includeClasses) {
                    if (includeClass.equals(clazz)) {
                        include = true;
                        break;
                    }
                }
            } else if (filterType.equals(FilterType.CUSTOM)) {
                for (Class<?> includeClass : includeClasses) {
                    try {
                        Method match = includeClass.getDeclaredMethod("match", Class.class);
                        boolean isAgree = (boolean) match.invoke(includeClass.newInstance(),clazz);
                        if (isAgree) {
                            include = true;
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return !exclude && include;
    }
}
