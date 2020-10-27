package org.jdragon.springz.scanner.entry;

import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.27 12:34
 * @Description:
 */
public class WaitBeanInfo {

    private static final Logger logger = LoggerFactory.getLogger(WaitBeanInfo.class);

    private final Object waitBean;

    private final Method constructMethod;

    private final String scope;

    private final List<String> paramsNameList;

    private final List<String> needBeanName;

    private final String[] beanNames;

    public WaitBeanInfo(String[] beanNames, Object waitBean, Method constructMethod, String scope, List<String> paramsNameList, List<String> needBeanName) {
        this.beanNames = beanNames;
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

    public String[] getBeanNames() {
        return beanNames;
    }

    @Override
    public String toString() {
        return "WaitBeanInfo{" +
                "waitBean=" + waitBean +
                ", constructMethod=" + constructMethod +
                ", scope='" + scope + '\'' +
                ", paramsNameList=" + paramsNameList +
                ", needBeanName=" + needBeanName +
                ", beanNames=" + Arrays.toString(beanNames) +
                '}';
    }
}
