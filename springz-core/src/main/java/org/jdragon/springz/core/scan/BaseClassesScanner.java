package org.jdragon.springz.core.scan;


import org.jdragon.springz.core.annotation.IocMain;
import org.jdragon.springz.core.annotation.IocScan;
import org.jdragon.springz.core.container.BaseClassPackagesContainer;
import org.jdragon.springz.core.annotation.IocScans;
import org.jdragon.springz.core.entry.BasePackageInfo;

import org.jdragon.springz.core.manager.BaseClassPackagesManager;
import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.ClassInfo;

import java.lang.annotation.Annotation;

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
        //将springzMain加入
        if(clazz.isAnnotationPresent(IocMain.class)){
            BaseClassPackagesContainer.register(clazz.getPackage().getName(),new BasePackageInfo());
        }
        //扫描springzScans中的要扫描类
        if (clazz.isAnnotationPresent(IocScans.class)) {
            for (IocScan iocScan : clazz.getAnnotation(IocScans.class).value()) {
                BaseClassPackagesManager.resolverComponentScan(iocScan);
            }
        } else if (clazz.isAnnotationPresent(IocScan.class)) {
            BaseClassPackagesManager.resolverComponentScan(clazz.getAnnotation(IocScan.class));
        }

        //扫描因扩展功能放在enable中的springzScan
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
