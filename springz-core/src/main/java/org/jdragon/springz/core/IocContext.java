package org.jdragon.springz.core;

import org.jdragon.springz.core.manager.BaseClassPackagesManager;
import org.jdragon.springz.core.manager.ScanActionManager;
import org.jdragon.springz.scanner.*;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.StrUtil;

import java.util.Map;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:05
 * @Description: 组件管理容器类
 */

public class IocContext {

    private final static Logger logger = LoggerFactory.getLogger(IocContext.class);

    private final static Map<String, BeanInfo> beanMap = BeanContainer.getBeanMap();


    public static void run(Class<?> baseClazz) {
        run(baseClazz.getPackage().getName());
    }

    public static void run(String basePackageName) {
        long start = System.currentTimeMillis();

        //获取要扫描的基础路径集
        String[] scanBasePackages = BaseClassPackagesManager.scanBaseClasses(basePackageName);

        //根据扫描集来新建扫描器，一旦创建则确认扫描器扫描范围
        ScanActionManager.initScan(scanBasePackages);

        //注册在扫描时要做的行为
        ScanActionManager.registerScanAction();

        //开始扫描
        ScanActionManager.doScan();

        //打印启动最终结果
        BeanContainer.printResult();

        logger.info("启动所用时间", System.currentTimeMillis() - start + "ms");
    }


    public static Object getBean(String key) {
        if (beanMap.containsKey(key)) {
            return beanMap.get(key).getBean();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        String simple = StrUtil.firstLowerCase(clazz.getSimpleName());
        BeanInfo beanInfo = beanMap.get(simple);
        beanInfo = beanInfo != null ? beanInfo : beanMap.get(clazz.getName());
        if (beanInfo == null || beanInfo.getBean() == null) {
            return null;
        }
        return (T) beanInfo.getBean();
    }


    public static String[] getBeanDefinitionNames() {
        return beanMap.keySet().toArray(new String[0]);
    }

    public static Map<String, BeanInfo> getBeanMap() {
        return beanMap;
    }


    public static void close() {
        beanMap.clear();
    }
}
