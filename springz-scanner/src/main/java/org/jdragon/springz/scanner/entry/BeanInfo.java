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

    private final String definedName;

    private final Object bean;

    private final String scope;

    private final Class<?> clazz;

    private final String className;


    public BeanInfo(String definedName,Object bean, Class<?> clazz) {
        this.definedName = definedName;
        this.bean = bean;
        this.clazz = clazz;
        this.className = clazz.getName();
        this.scope = SINGLETON;
    }

    public BeanInfo(String definedName,Object bean, String scope, Class<?> clazz) {
        this.definedName = definedName;
        this.bean = bean;
        this.scope = scope;
        this.clazz = clazz;
        this.className = clazz.getName();
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

    public Class<?> getClazz() {
        return clazz;
    }

    public String getDefinedName() {
        return definedName;
    }
}
