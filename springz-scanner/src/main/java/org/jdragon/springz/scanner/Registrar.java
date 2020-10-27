package org.jdragon.springz.scanner;


import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.scanner.entry.WaitBeanInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.21 22:26
 * @Description:
 */
public abstract class Registrar {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static Map<String, BeanInfo> beanMap = new HashMap<>();

    protected static List<WaitBeanInfo> waitBeanList = new ArrayList<>();

    protected ClassInfo classInfo;

    /**
     * @params: [definitionName, obj]
     * @return: void
     * @Description: 通用注册类，将definitionName作为key,obj作为value存到beanMap中
     **/
    protected void register(String definitionName, Object obj, String scope) {
        //检查definitionName是否存在
        if (beanMap.containsKey(definitionName)) {
            Object existObj = beanMap.get(definitionName);
            logger.warn("已存在键名", definitionName, existObj.getClass().getName());
            logger.warn("请解决类键名冲突", definitionName, classInfo.getClassName());
            return;
        }
        //将对象放到map容器
        beanMap.put(definitionName, new BeanInfo(obj, scope));
        if (beanMap.containsKey(definitionName)) {
            logger.info("注册bean成功", definitionName, classInfo.getClassName());
        }
        checkWaitBean(definitionName);
    }

    //检查是否需要唤醒WaitBean
    private void checkWaitBean(String definitionName) {
        Iterator<WaitBeanInfo> iterator = waitBeanList.iterator();
        while (iterator.hasNext()) {
            WaitBeanInfo waitBeanInfo = iterator.next();
            List<String> needBeanName = waitBeanInfo.getNeedBeanName();
            if (needBeanName.remove(definitionName) && needBeanName.isEmpty()) {
                awakeWaitBean(waitBeanInfo);
                iterator.remove();
            }
        }
    }

    //唤醒等待注册的bean
    private void awakeWaitBean(WaitBeanInfo waitBeanInfo) {
        List<String> paramsNameList = waitBeanInfo.getParamsNameList();
        Object[] needBean = paramsNameList.stream()
                .map(e -> beanMap.get(e).getBean())
                .toArray();
        Object bean = waitBeanInfo.createBean(needBean);
        if (bean == null) return;
        for (String beanName : waitBeanInfo.getBeanNames()) {
            register(beanName, bean, waitBeanInfo.getScope());
            logger.warn("唤醒出列:" + beanName);
        }
    }

    public static Map<String, BeanInfo> getBeanMap() {
        return beanMap;
    }

    public static List<WaitBeanInfo> getWaitBeanList() {
        return waitBeanList;
    }

    public void addWaitBean(WaitBeanInfo waitBeanInfo) {
        waitBeanList.add(waitBeanInfo);
    }
}
