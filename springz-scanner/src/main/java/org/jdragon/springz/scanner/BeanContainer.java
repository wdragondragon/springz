package org.jdragon.springz.scanner;

import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.WaitBeanInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.04 16:42
 * @Description:
 */
public class BeanContainer {
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
}
