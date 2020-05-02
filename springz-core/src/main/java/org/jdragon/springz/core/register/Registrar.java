package org.jdragon.springz.core.register;

import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.entry.BeanInfo;
import org.jdragon.springz.core.entry.ClassInfo;
import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.02 11:39
 * @Description: 通用注册器
 */
public abstract class Registrar {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Map<String, BeanInfo> beanMap;

    protected ClassInfo classInfo;


    protected Registrar(Map<String, BeanInfo> beanMap){
        this.beanMap = beanMap;
    }

    /**
     * @params: [definitionName, obj]
     * @return: void
     * @Description: 通用注册类，将definitionName作为key,obj作为value存到beanMap中
     **/
    public void register(String definitionName, Object obj,String scope) {
        //检查definitionName是否存在
        if (beanMap.containsKey(definitionName)) {
            Object existObj = beanMap.get(definitionName);
            logger.warn("已存在键名",definitionName,existObj.getClass().getName());
            logger.warn("请解决类键名冲突",definitionName, classInfo.getClassName());
            return;
        }
        //将对象放到map容器
        beanMap.put(definitionName, new BeanInfo(obj,scope));
        if (beanMap.containsKey(definitionName)) {
            logger.info("注册bean成功",definitionName, classInfo.getClassName());
        }
    }
    /**
     * @params: [c]
     * @return: java.lang.String
     * @Description:
     *         这个方法的返回值决定了Registrar是否需要注册，同时返回的value会作为component的名称
     *         返回值为Null之后，意味着不用注册
     *         判断类是否为注解，如果是则不需要注册
     *         从类中检测是否有Component注解
     *         若无，再检查类中所有的注解是否有包含Component注解（注解嵌套）
     *         返回的value是null，说明他所有注解和Component无关，就不需要注册
     **/
    public String getComponentValue(Class<?> c) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //判断类是否为注解，如果是则不需要注册
        if (c.isAnnotation()) {
            return null;
        }
        String value;
        if (c.isAnnotationPresent(Component.class)) {
            value = AnnotationUtils.getAnnotationAttribute(c.getAnnotation(Component.class), "value");
        } else {
            value = AnnotationUtils.getAnnotationValue(c,Component.class);
        }
        return value;
    }
}
