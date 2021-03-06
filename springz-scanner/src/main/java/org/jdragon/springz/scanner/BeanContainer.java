package org.jdragon.springz.scanner;

import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.InjectedObject;
import org.jdragon.springz.scanner.entry.WaitBeanInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.04 16:42
 * @Description:
 */
public class BeanContainer {

    private final static Logger logger = LoggerFactory.getLogger(BeanContainer.class);

    //已成功注册的bean
    private static final Map<String, BeanInfo> beanMap = new ConcurrentHashMap<>();
    //缺失依赖的bean
    private static final List<WaitBeanInfo> waitBeanList = new CopyOnWriteArrayList<>();

    //已注入的对象实例，用来结束程序时执行销毁方法等
    private static final List<InjectedObject> injectedObjectList = new LinkedList<>();

    public static Map<String, BeanInfo> getBeanMap() {
        return beanMap;
    }

    public static List<InjectedObject> getInjectedObjectList() {
        return injectedObjectList;
    }

    public static List<WaitBeanInfo> getWaitBeanList() {
        return waitBeanList;
    }

    public static void printResult() {
        logger.warn("最后仍未注册成功的Bean", Arrays.toString(waitBeanList.stream()
                .map(WaitBeanInfo::getBeanName)
                .toArray(String[]::new)));

        logger.warn("已注册bean列表", Arrays.toString(beanMap.keySet().toArray(new String[0])));
    }
}
