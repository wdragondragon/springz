package org.jdragon.springz.core;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:05
 * @Description: 组件管理容器类
 */

import org.jdragon.springz.core.entry.BeanInfo;
import org.jdragon.springz.core.scan.*;
import org.jdragon.springz.utils.LogBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class AnnotationApplicationContext implements AnnotationResolver,AnnotationRegister{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, BeanInfo> beanMap;

    private Scanner scanner;

    public AnnotationApplicationContext(Class<?> baseClazz) {
        long start = System.currentTimeMillis();

        String[] baseClassesName = {baseClazz.getPackage().getName()};

        scanner = new Scanner(baseClassesName);

        //扫描包和注册组件
        scanAndRegister();

        //注入Bean
        injection();

        logger.info(LogBuilder.build("启动所用时间",System.currentTimeMillis()-start+"ms"));
    }

    @Override
    public void scanAndRegister() {
        Registrar registrar = new Registrar();
        scanner.setAction(registrar).doScan();
        beanMap = registrar.getBeanOfAll();
    }

    @Override
    public void injection() {
        Infuser infuser = new Infuser(beanMap);
        scanner.setAction(infuser).doScan();
    }

    public Map<String, BeanInfo> getBeanOfAll() {
        return beanMap;
    }

    public Object getBean(String key) {
        return beanMap.get(key);
    }

    public String[] getBeanDefinitionNames() {
        return beanMap.keySet().toArray(new String[0]);
    }

    public void close() {
        beanMap.clear();
        beanMap = null;
    }
}
