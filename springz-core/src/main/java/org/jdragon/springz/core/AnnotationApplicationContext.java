package org.jdragon.springz.core;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:05
 * @Description: 组件管理容器类
 */

import org.jdragon.springz.core.entry.BeanInfo;
import org.jdragon.springz.core.filter.BaseFilter;
import org.jdragon.springz.core.filter.Filter;
import org.jdragon.springz.core.infuse.Infuser;
import org.jdragon.springz.core.register.MethodRegistrar;
import org.jdragon.springz.core.register.TypeRegistrar;
import org.jdragon.springz.core.scan.*;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class AnnotationApplicationContext {

    private final static Logger logger = LoggerFactory.getLogger(AnnotationApplicationContext.class);

    private final BaseClassesScanContext baseClassesScanContext = new BaseClassesScanContext();

    private Map<String, BeanInfo> beanMap = new HashMap<>();

    public AnnotationApplicationContext(Class<?> baseClazz) {
        long start = System.currentTimeMillis();

        //获取主方法的基础路径集
        String[] baseClassesName = {baseClazz.getPackage().getName()};

        //获取要扫描的基础路径集
        String[] scanBasePackages = getBaseClassesName(baseClassesName);

        //根据扫描集来新建扫描器，一旦创建则确认扫描器扫描范围
        Scanner scanner = newScan(scanBasePackages);

        //@Component扫描注册
        scanner.setAction(new TypeRegistrar(beanMap)).doScan();

        //@Bean扫描注册
        scanner.setAction(new MethodRegistrar(beanMap)).doScan();

        //注入
        scanner.setAction(new Infuser(beanMap)).doScan();

        logger.info("启动所用时间", System.currentTimeMillis() - start + "ms");
    }

    public String[] getBaseClassesName(String... baseClassesName) {
        new Scanner().setAction(baseClassesScanContext).doScan();
        return baseClassesScanContext.getBasePackages(baseClassesName);
    }

    public Scanner newScan(String[] scanBasePackages) {
        Filter baseFilter = new BaseFilter(baseClassesScanContext.getBasePackageInfoMap());
        return new Scanner(scanBasePackages).addFilter(baseFilter);
    }

    public Map<String, BeanInfo> getBeanOfAll() {
        return beanMap;
    }

    public Object getBean(String key) {
        if (beanMap.containsKey(key)) {
            return beanMap.get(key).getBean();
        }
        return null;
    }

    public <T> T getBean(Class<T> clazz) {
        String simple = StringUtils.firstLowerCase(clazz.getSimpleName());
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
