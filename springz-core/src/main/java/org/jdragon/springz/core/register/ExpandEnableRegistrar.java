package org.jdragon.springz.core.register;

import org.jdragon.springz.core.annotation.SpringzScan;
import org.jdragon.springz.core.BaseClassesScanContext;
import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.ClassInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.22 14:05
 * @Description:
 */
public class ExpandEnableRegistrar implements ScanAction {

    BaseClassesScanContext scanAction;

    public ExpandEnableRegistrar(BaseClassesScanContext scanAction){
        this.scanAction = scanAction;
    }

    @Override
    public void action(ClassInfo classInfo) {
        SpringzScan springzScan = (SpringzScan) AnnotationUtils.getContainedAnnotationType(classInfo.getClazz(), SpringzScan.class);
        if (springzScan != null)
            scanAction.resolverComponentScan(springzScan);
    }

    @Override
    public Filter[] getFilters() {
        return scanAction.getFilters();
    }
}
