package org.jdragon.springz.scanner.entry;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author JDragon
 * @Date 2021.03.02 上午 12:03
 * @Email 1061917196@qq.com
 * @Des: 已被注入的对象信息
 */
public class InjectedObject {
    private Object bean;

    private Class<?> clazz;

    private final List<Method> destroyMethods = new LinkedList<>();

    public void addDestroyMethod(Method method){
        destroyMethods.add(method);
    }

    public List<Method> getDestroyMethods() {
        return destroyMethods;
    }

    public InjectedObject(Object bean, Class<?> clazz) {
        this.bean = bean;
        this.clazz = clazz;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "InjectedObject{" +
                "bean=" + bean +
                ", clazz=" + clazz +
                '}';
    }
}
