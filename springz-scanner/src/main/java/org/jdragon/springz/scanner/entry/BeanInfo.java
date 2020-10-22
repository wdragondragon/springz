package org.jdragon.springz.scanner.entry;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 18:39
 * @Description: bean的信息
 */
public class BeanInfo{

    public final static String SINGLETON = "singleton";

    public final static String PROTOTYPE = "prototype";

    private Object bean;

    private String scope;

    public BeanInfo(Object bean) {
        this.bean = bean;
        this.scope = SINGLETON;
    }

    public BeanInfo(Object bean, String scope) {
        this.bean = bean;
        this.scope = scope;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
