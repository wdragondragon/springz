package org.jdragon.springz.core;

import org.jdragon.springz.core.filter.BaseFilter;
import org.jdragon.springz.core.infuse.Infuser;
import org.jdragon.springz.core.register.*;
import org.jdragon.springz.core.scan.BaseClassesScanner;
import org.jdragon.springz.scanner.*;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.WaitBeanInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:05
 * @Description: 组件管理容器类
 */

public class SpringzContext {

    private final static Logger logger = LoggerFactory.getLogger(SpringzContext.class);

    private final static Map<String, BeanInfo> beanMap = Registrar.getBeanMap();

    private final static List<WaitBeanInfo> waitBeanInfoList = Registrar.getWaitBeanList();

    private final static BaseClassesScanner baseClassesScanner = new BaseClassesScanner();

    private static Scanner scanner;

    public static void run(Class<?> baseClazz) {
        run(baseClazz.getPackage().getName());
    }

    public static void run(String... basePackageName) {
        long start = System.currentTimeMillis();

        //获取要扫描的基础路径集
        String[] scanBasePackages = getBaseClassesName(basePackageName);

        //根据扫描集来新建扫描器，一旦创建则确认扫描器扫描范围
        scanner = newScan(scanBasePackages);

        //注册在扫描时要做的行为
        registerScanAction();

        //开始扫描
        doScan();

        //打印缺失依赖导致未能注册成功的Bean名字
        printWaitBeanInfo();

        logger.info("启动所用时间", System.currentTimeMillis() - start + "ms");
    }

    public static String[] getBaseClassesName(String... baseClassesName) {
        Scanner scanner = newScan(baseClassesName);
        scanner.action(baseClassesScanner).doScan();
        String[] basePackages = BaseClassPackagesManager.getBasePackages(baseClassesName);
        logger.info("基础包扫描区域：", Arrays.toString(basePackages));

        //拓展enable注册器
        scanner.action(new ExpandEnableRegistrar(baseClassesScanner)).doScan();
        basePackages = BaseClassPackagesManager.getBasePackages(baseClassesName);
        logger.info("全部包扫描区域：", Arrays.toString(basePackages));

        return BaseClassPackagesManager.getBasePackages(baseClassesName);
    }

    private static Scanner newScan(String... scanBasePackages) {
        return new Scanner(scanBasePackages);
    }

    private static void registerScanAction() {
        Filter baseFilter = new BaseFilter();

        ScanManager.registerScanAction(new TypeComponentRegistrar(baseFilter));//@Component扫描注册

        ScanManager.registerScanAction(new MethodComponentRegistrar(baseFilter));  //@Bean扫描注册

        ScanManager.registerScanAction(new PostProcessorRegistrar());//加载bean初始化完成的后置处理器

        scanner.action(new ActionRegistrar()).doScan(); //拓展注册器

        ScanManager.registerScanAction(new Infuser(baseFilter));//注入
    }

    private static void doScan() {
        ScanManager.getScanActionList().forEach(scanAction -> scanner.action(scanAction).doScan());
    }


    private static void printWaitBeanInfo() {
        logger.warn("最后仍未注册成功的Bean", Arrays.toString(waitBeanInfoList.stream()
                .map(WaitBeanInfo::getBeanName)
                .toArray(String[]::new)));
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

    public static List<WaitBeanInfo> getWaitBeanInfoList() {
        return waitBeanInfoList;
    }

    public static void close() {
        beanMap.clear();
    }
}
