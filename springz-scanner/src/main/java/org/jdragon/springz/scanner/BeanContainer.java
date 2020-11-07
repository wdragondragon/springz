package org.jdragon.springz.scanner;

import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.WaitBeanInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.04 16:42
 * @Description:
 */
public class BeanContainer {

    private final static Logger logger = LoggerFactory.getLogger(BeanContainer.class);

    //已成功注册的bean
    private static final Map<String, BeanInfo> beanMap = new HashMap<>();
    //缺失依赖的bean
    private static final List<WaitBeanInfo> waitBeanList = new ArrayList<>();

    public static Map<String, BeanInfo> getBeanMap() {
        return beanMap;
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
