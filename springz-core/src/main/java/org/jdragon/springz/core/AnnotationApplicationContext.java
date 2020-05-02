package org.jdragon.springz.core;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.23 19:05
 * @Description: 组件管理容器类
 */

import org.jdragon.springz.core.entry.BeanInfo;
import org.jdragon.springz.core.register.MethodRegistrar;
import org.jdragon.springz.core.register.TypeRegistrar;
import org.jdragon.springz.core.scan.*;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class AnnotationApplicationContext implements AnnotationResolver,AnnotationRegister{

    private final static Logger logger = LoggerFactory.getLogger(AnnotationApplicationContext.class);

    private Map<String, BeanInfo> beanMap = new HashMap<>();

    private Scanner scanner;

    public AnnotationApplicationContext(Class<?> baseClazz) {
        long start = System.currentTimeMillis();

        String[] baseClassesName = {baseClazz.getPackage().getName()};

        scanner = new Scanner(baseClassesName);

        //@Component扫描注册
        registerType();

        //@Bean扫描注册
        registerMethod();

        //注入
        injection();

        logger.info("启动所用时间",System.currentTimeMillis()-start+"ms");
    }

    public void registerMethod(){
        scanner.setAction(new MethodRegistrar(beanMap)).doScan();
    }

    @Override
    public void registerType() {
        scanner.setAction(new TypeRegistrar(beanMap)).doScan();
    }

    @Override
    public void injection() {
        scanner.setAction(new Infuser(beanMap)).doScan();
    }

    public Map<String, BeanInfo> getBeanOfAll() {
        return beanMap;
    }

    public Object getBean(String key) {
        if(beanMap.containsKey(key)){
            return beanMap.get(key).getBean();
        }
        return null;
    }

    public Object getBean(Class<?> clazz) {
        String simple = StringUtils.firstLowerCase(clazz.getSimpleName());
        BeanInfo beanInfo = beanMap.get(simple);
        beanInfo = beanInfo!=null?beanInfo:beanMap.get(clazz.getName());
        if (beanInfo==null){
            return null;
        }
        return beanInfo.getBean();
    }


    public String[] getBeanDefinitionNames() {
        return beanMap.keySet().toArray(new String[0]);
    }

    public void close() {
        beanMap.clear();
        beanMap = null;
    }
}
