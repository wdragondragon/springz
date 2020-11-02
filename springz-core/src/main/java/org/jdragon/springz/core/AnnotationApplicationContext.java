package org.jdragon.springz.core;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:05
 * @Description: 组件管理容器类
 */

import org.jdragon.springz.core.register.*;
import org.jdragon.springz.scanner.Registrar;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.core.filter.BaseFilter;

import org.jdragon.springz.core.infuse.Infuser;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.Scanner;
import org.jdragon.springz.scanner.entry.WaitBeanInfo;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.StrUtil;

import java.util.*;


public class AnnotationApplicationContext {

    private final static Logger logger = LoggerFactory.getLogger(AnnotationApplicationContext.class);

    private final BaseClassesScanContext baseClassesScanContext = new BaseClassesScanContext();

    private Map<String, BeanInfo> beanMap = Registrar.getBeanMap();

    private List<WaitBeanInfo> waitBeanInfoList = Registrar.getWaitBeanList();

    private final List<ScanAction> scanActionList = new LinkedList<>();

    private final Scanner scanner;

    public AnnotationApplicationContext(Class<?> baseClazz) {
        //获取主方法的基础路径集
        this(baseClazz.getPackage().getName());
    }

    public AnnotationApplicationContext(String... basePackageName) {
        long start = System.currentTimeMillis();

        //获取要扫描的基础路径集
        String[] scanBasePackages = getBaseClassesName(basePackageName);

        //根据扫描集来新建扫描器，一旦创建则确认扫描器扫描范围
        scanner = newScan(scanBasePackages);

        initScan();

        doScan();

        printWaitBeanInfo();

        logger.info("启动所用时间", System.currentTimeMillis() - start + "ms");
    }


    private void initScan() {
        Filter baseFilter = new BaseFilter(baseClassesScanContext.getBasePackageInfoMap());

        scanActionList.add(new TypeComponentRegistrar(baseFilter));        //@Component扫描注册

        scanActionList.add(new MethodComponentRegistrar(baseFilter));        //@Bean扫描注册

        scanActionList.add(new PostProcessorRegistrar()); //加载bean初始化完成的后置处理器

        scanner.action(new ActionRegistrar(scanActionList)).doScan(); //拓展注册器

        scanActionList.add(new Infuser(baseFilter));        //注入
    }

    private void doScan() {
        scanActionList.forEach(scanAction -> scanner.action(scanAction).doScan());
    }

    public String[] getBaseClassesName(String... baseClassesName) {
        Scanner scanner = newScan(baseClassesName);
        scanner.action(baseClassesScanContext).doScan();
        String[] basePackages = baseClassesScanContext.getBasePackages(baseClassesName);
        logger.info("基础包扫描区域：", Arrays.toString(basePackages));

        //拓展enable注册器
        scanner.action(new ExpandEnableRegistrar(baseClassesScanContext)).doScan();
        basePackages = baseClassesScanContext.getBasePackages(baseClassesName);
        logger.info("全部包扫描区域：", Arrays.toString(basePackages));

        return baseClassesScanContext.getBasePackages(baseClassesName);
    }

    private Scanner newScan(String... scanBasePackages) {
        return new Scanner(scanBasePackages);
    }

    private void printWaitBeanInfo() {
        List<String> waitBeans = new ArrayList<>();
        waitBeanInfoList.forEach(waitBeanInfo -> waitBeans.add(waitBeanInfo.getBeanName()));
        logger.warn("最后仍未注册成功的Bean", waitBeans.toString());
    }

    public Map<String, BeanInfo> getBeans() {
        return beanMap;
    }

    public Object getBean(String key) {
        if (beanMap.containsKey(key)) {
            return beanMap.get(key).getBean();
        }
        return null;
    }

    public <T> T getBean(Class<T> clazz) {
        String simple = StrUtil.firstLowerCase(clazz.getSimpleName());
        BeanInfo beanInfo = beanMap.get(simple);
        beanInfo = beanInfo != null ? beanInfo : beanMap.get(clazz.getName());
        if (beanInfo == null || beanInfo.getBean() == null) {
            return null;
        }
        return (T) beanInfo.getBean();
    }


    public String[] getBeanDefinitionNames() {
        return beanMap.keySet().toArray(new String[0]);
    }

    public void close() {
        beanMap.clear();
        beanMap = null;
    }
}
