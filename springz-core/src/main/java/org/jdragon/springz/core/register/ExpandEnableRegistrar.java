package org.jdragon.springz.core.register;

import org.jdragon.springz.core.annotation.IocScan;
import org.jdragon.springz.core.manager.BaseClassPackagesManager;
import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.ClassInfo;

import java.lang.annotation.Annotation;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.22 14:05
 * @Description: 扫描拓展功能的@Enable注解的扫描器
 */
public class ExpandEnableRegistrar implements ScanAction {
    @Override
    public void action(ClassInfo classInfo) {
        Class<?> clazz = classInfo.getClazz();
        for (Annotation annotation : clazz.getAnnotations()) {
            IocScan iocScan = (IocScan) AnnotationUtils
                    .getAllContainedAnnotationType(annotation.annotationType(), IocScan.class);
            if (iocScan != null) {
                BaseClassPackagesManager.resolverComponentScan(iocScan);
            }
        }
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }
}
