package org.jdragon.springz.scanner.entry;

import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.27 12:34
 * @Description: 在第一次注册@bean时所需要的依赖组件不足时，会生成一个WaitBeanInfo加入唤醒队列
 */
public class WaitBeanInfo {

    private static final Logger logger = LoggerFactory.getLogger(WaitBeanInfo.class);

    private final Object waitBean;

//    private final String className;

    private final Class<?> clazz;

    private final Method constructMethod;

    private final String scope;

    private final List<String> paramsNameList;

    private final List<String> needBeanName;

    private final String beanName;

    public WaitBeanInfo(String beanName, Class<?> clazz,Object waitBean, Method constructMethod, String scope, List<String> paramsNameList, List<String> needBeanName) {
        this.beanName = beanName;
        this.clazz = clazz;
        this.waitBean = waitBean;
        this.constructMethod = constructMethod;
        this.scope = scope;
        this.paramsNameList = paramsNameList;
        this.needBeanName = needBeanName;
    }

    public Object createBean(Object[] needBean) {
        try {
            return constructMethod.invoke(waitBean, needBean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("唤醒注册bean失败", "虽已满足参数条件，但是构造失败", waitBean.getClass().getName(), constructMethod.getName());
            e.printStackTrace();
            return null;
        }
    }

    public String getScope() {
        return scope;
    }

    public List<String> getParamsNameList() {
        return paramsNameList;
    }

    public List<String> getNeedBeanName() {
        return needBeanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return "WaitBeanInfo{" +
                "waitBean=" + waitBean +
                ", clazz=" + clazz +
                ", constructMethod=" + constructMethod +
                ", scope='" + scope + '\'' +
                ", paramsNameList=" + paramsNameList +
                ", needBeanName=" + needBeanName +
                ", beanName='" + beanName + '\'' +
                '}';
    }
}
