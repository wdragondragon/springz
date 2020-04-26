package org.jdragon.springz.core.entry;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 17:55
 * @Description: 扫描到的包的信息
 */
public class ClassInfo {

    private String definitionName;

    private String className;

    private Class<?> clazz;

    public ClassInfo() {
    }

    public ClassInfo(String definitionName, String className) {
        this();
        this.definitionName = definitionName;
        this.className = className;
    }

    public ClassInfo(String definitionName, String className, Class<?> clazz) {
        this(definitionName,className);
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }


    public String getDefinitionName() {
        return definitionName;
    }

    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "BeanInfo{" +
                "key='" + definitionName + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
