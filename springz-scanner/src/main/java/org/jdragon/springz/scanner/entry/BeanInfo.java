package org.jdragon.springz.scanner.entry;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 18:39
 * @Description: bean的信息
 */
public class BeanInfo {

    public final static String SINGLETON = "singleton";

    public final static String PROTOTYPE = "prototype";

    private final Object bean;

    private final String scope;

    private final String className;

    public BeanInfo(Object bean, String className) {
        this.bean = bean;
        this.className = className;
        this.scope = SINGLETON;
    }

    public BeanInfo(Object bean, String scope, String className) {
        this.bean = bean;
        this.scope = scope;
        this.className = className;
    }

    public Object getBean() {
        return bean;
    }


    public String getScope() {
        return scope;
    }

    public boolean isSingleton() {
        return SINGLETON.equals(scope);
    }

    public boolean isPrototype() {
        return PROTOTYPE.equals(scope);
    }

    public String getClassName() {
        return className;
    }
}
