package org.jdragon.springz.core.register;


import org.jdragon.springz.core.infuse.Infuser;
import org.jdragon.springz.scanner.BeanContainer;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.scanner.entry.WaitBeanInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.StrUtil;

import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.21 22:26
 * @Description:
 */
public abstract class Registrar {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 放置已注册组件
     */
    protected final Map<String, BeanInfo> beanMap = BeanContainer.getBeanMap();

    /**
     * 放置因依赖缺失的等待注册组件
     */
    protected final List<WaitBeanInfo> waitBeanList = BeanContainer.getWaitBeanList();

    /**
     * 注册时，完全可使用classInfo中的属性
     * @param classInfo 扫描包时提供的类信息
     * @param obj 注册实例
     * @param scope 注册策略
     */
    protected void register(ClassInfo classInfo, Object obj, String scope) {
        register(classInfo.getDefinitionName(), classInfo.getClassName(), obj, scope);
        register(classInfo.getClazz(), obj, scope);
    }

    /**
     * 注册时，可自定义组件名
     * @param definitionName 自定义组件名
     * @param classInfo 扫描包时提供的类信息
     * @param obj 注册实例
     * @param scope 注册策略
     */
    protected void register(ClassInfo classInfo, String definitionName, Object obj, String scope) {
        register(definitionName, classInfo.getClassName(), obj, scope);
        register(classInfo.getClazz(), obj, scope);
    }

    /**
     *
     * @param clazz 自定义类
     * @param definitionName 自定义组件名
     * @param obj 注册实例
     * @param scope 注册策略
     */
    protected void register(Class<?> clazz, String definitionName, Object obj, String scope) {
        register(definitionName, clazz.getName(), obj, scope);
        register(clazz, obj, scope);
    }

    /**
     * 公共注册类，都要调用来做递归进行父类或接口注册
     * @param clazz 自定义类
     * @param obj 注册实例
     * @param scope 注册策略
     */
    protected void register(Class<?> clazz, Object obj, String scope) {
        Class<?>[] interfaces = clazz.getInterfaces();

        for (Class<?> anInterface : interfaces) {
            String interfaceSimpleName = anInterface.getSimpleName();
            register(StrUtil.firstLowerCase(interfaceSimpleName), anInterface.getName(), obj, scope);
        }

        Class<?> superclass = clazz.getSuperclass();

        if (superclass != null && !superclass.equals(Object.class)) {
            String superclassSimpleName = superclass.getSimpleName();
            register(StrUtil.firstLowerCase(superclassSimpleName), superclass.getName(), obj, scope);
        }
    }

    /**
     *
     * @params: [definitionName, obj]
     * @return: void
     * @Description: 通用注册类，将definitionName作为key,obj作为value存到beanMap中
     **/
    private void register(String definitionName, String className, Object obj, String scope) {
        //检查definitionName是否存在
        if (beanMap.containsKey(definitionName)) {
            BeanInfo beanInfo = beanMap.get(definitionName);
            //相同时则不报异常
            if (beanInfo.getClassName().equals(className)) return;
            logger.warn("已存在键名[键名][冲突类名]", definitionName, beanInfo.getClassName());
            logger.warn("请解决类键名冲突[键名][类名]", definitionName, className);
            return;
        }
        //将对象放到map容器
        beanMap.put(definitionName, new BeanInfo(obj, scope, className));
        if (beanMap.containsKey(definitionName)) {
            logger.info("注册bean成功[键名][类名]", definitionName, className);
        }

        awakeWaitBeansByDefinitionName(definitionName);
    }


    public void addWaitBean(WaitBeanInfo waitBeanInfo) {
        waitBeanList.add(waitBeanInfo);
    }

    /**
     * 根据这次注册的组件名来唤醒等待队列出队
     * @param definitionName 该次注册的组件名
     */
    private void awakeWaitBeansByDefinitionName(String definitionName) {
        List<WaitBeanInfo> needAwakeBeans = getNeedAwakeBean(definitionName);
        for (WaitBeanInfo needAwakeBean : needAwakeBeans) {
            awakeWaitBean(needAwakeBean);
        }
        waitBeanList.removeAll(needAwakeBeans);
    }

    /**
     * 获取需要唤醒的组件列表
     * @param definitionName 该次注册的组件名
     * @return 获取需要唤醒WaitBean
     * {@link this#awakeWaitBeansByDefinitionName(String)} only call by there
     */
    private List<WaitBeanInfo> getNeedAwakeBean(String definitionName) {
        List<WaitBeanInfo> needAwakeBeans = new ArrayList<>();

        for (WaitBeanInfo waitBeanInfo : waitBeanList) {
            List<String> needBeanName = waitBeanInfo.getNeedBeanName();
            if (needBeanName.remove(definitionName) && needBeanName.isEmpty()) {
                needAwakeBeans.add(waitBeanInfo);
            }
        }
        return needAwakeBeans;
    }

    /**
     * 唤醒等待注册的bean
     * @param waitBeanInfo 需要唤醒的组件信息
     * {@link this#awakeWaitBeansByDefinitionName(String)} only call by there
     */
    private void awakeWaitBean(WaitBeanInfo waitBeanInfo) {
        List<String> paramsNameList = waitBeanInfo.getParamsNameList();

        Infuser infuser = new Infuser();

        Object[] needBean = paramsNameList.stream()
                .map(e -> infuser.createAnalyzeBean(e, e.getClass()))
                .toArray();
        Object bean = waitBeanInfo.createBean(needBean);
        if (bean == null) return;

        String beanName = waitBeanInfo.getBeanName();
        register(beanName, waitBeanInfo.getClassName(), bean, waitBeanInfo.getScope());
        logger.warn("唤醒出列:" + beanName);
    }
}