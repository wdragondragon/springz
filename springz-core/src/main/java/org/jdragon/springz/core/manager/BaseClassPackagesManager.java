package org.jdragon.springz.core.manager;

import org.jdragon.springz.core.annotation.IocScan;
import org.jdragon.springz.core.container.BaseClassPackagesContainer;
import org.jdragon.springz.core.entry.BasePackageInfo;
import org.jdragon.springz.core.entry.FilterMeta;
import org.jdragon.springz.core.scan.BaseClassesScanner;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.util.Arrays;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.08 00:24
 * @Description:
 */
public class BaseClassPackagesManager {

    private final static Logger logger = LoggerFactory.getLogger(BaseClassPackagesManager.class);

    /**
     * 通过springzScan注解实例来获取需要扫描的包信息
     * @param iocScan 注解实例
     */
    public static void resolverComponentScan(IocScan iocScan) {
        Class<?>[] basePackageClasses = iocScan.basePackageClasses();
        String[] basePackages = iocScan.basePackage();
        boolean useDefaultFilters = iocScan.useDefaultFilters();
        IocScan.ComponentFilter[] excludeComponentFilters = iocScan.excludeFilters();
        IocScan.ComponentFilter[] includeComponentFilters = iocScan.includeFilters();

        FilterMeta[] includeFiltersInfo = new FilterMeta[includeComponentFilters.length];
        FilterMeta[] excludeFiltersInfo = new FilterMeta[excludeComponentFilters.length];

        int i = 0;
        for (IocScan.ComponentFilter componentFilter : includeComponentFilters) {
            includeFiltersInfo[i] = new FilterMeta(componentFilter.type(), componentFilter.classes());
            i++;
        }
        i = 0;
        for (IocScan.ComponentFilter componentFilter : excludeComponentFilters) {
            excludeFiltersInfo[i] = new FilterMeta(componentFilter.type(), componentFilter.classes());
            i++;
        }

        BasePackageInfo basePackageInfo =
                new BasePackageInfo(useDefaultFilters, includeFiltersInfo, excludeFiltersInfo);
        for (Class<?> basePackageClass : basePackageClasses) {
            String basePackage = basePackageClass.getPackage().getName();
            BaseClassPackagesContainer.register(basePackage, basePackageInfo);
        }
        for (String basePackage : basePackages) {
            BaseClassPackagesContainer.register(basePackage, basePackageInfo);
        }
    }

    public static String[] scanBaseClasses(String baseClassesName) {
        ScanActionManager
                .newScan(baseClassesName)
                .action(new BaseClassesScanner())
                .doScan();
        String[] basePackagesName = BaseClassPackagesContainer.getBasePackagesName(baseClassesName);
        logger.info("包扫描区域：", Arrays.toString(basePackagesName));
        return basePackagesName;
    }
}
