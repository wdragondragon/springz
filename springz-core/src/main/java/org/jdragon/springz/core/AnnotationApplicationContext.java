package org.jdragon.springz.core;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:05
 * @Description: 组件管理容器类
 */



import org.jdragon.springz.core.entry.BeanInfo;
import org.jdragon.springz.core.scan.Infuser;
import org.jdragon.springz.core.scan.Registrar;
import org.jdragon.springz.core.scan.Scanner;

import java.util.HashMap;
import java.util.Map;


public class AnnotationApplicationContext implements AnnotationRegister {

    private Map<String, BeanInfo> beanMap = new HashMap<>();

    private Scanner scanner;

    public AnnotationApplicationContext(Class<?> baseClazz) {

        scanner = new Scanner(baseClazz);

        //扫描包和注册组件
        scanAndRegister();

        //注入Bean
        injection();

    }

    @Override
    public void scanAndRegister() {
        Registrar registrar = new Registrar();
        scanner.setAction(registrar).action();
        beanMap = registrar.getBeanOfAll();
    }

    @Override
    public void injection() {
        Infuser infuser = new Infuser(beanMap);
        scanner.setAction(infuser).action();
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
