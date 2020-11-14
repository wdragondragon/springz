package org.jdragon.springz.core.utils;

import lombok.SneakyThrows;
import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.core.annotation.Resource;
import org.jdragon.springz.utils.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.11 13:56
 * @Description:
 */
public class BeanHelper {
    /**
     * @params: [c]
     * @return: java.lang.String
     * @Description: 这个方法的返回值决定了Registrar是否需要注册，同时返回的value会作为component的名称
     * 返回值为Null之后，意味着不用注册
     * 判断类是否为注解，如果是则不需要注册
     * 从类中检测是否有Component注解
     * 若无，再检查类中所有的注解是否有包含Component注解（注解嵌套）
     * 返回的value是null，说明他所有注解和Component无关，就不需要注册
     **/
    @SneakyThrows
    public static String getComponentValue(Class<?> c) {
        //判断类是否为注解，如果是则不需要注册
        if (c.isAnnotation()) {
            return null;
        }
        String value;
        if (c.isAnnotationPresent(Component.class)) {
            value = (String) AnnotationUtils.getAnnotationAttribute(c.getAnnotation(Component.class), "value");
        } else {
            value = (String) AnnotationUtils.getIncludeAnnotationValue(c, Component.class, "value");
        }
        return value;
    }


    public static String getClassBeanName(Class<?> clazz){
        String proxyBeanName = getComponentValue(clazz);
        proxyBeanName = proxyBeanName.isEmpty() ? StrUtil.firstLowerCase(clazz.getSimpleName()) : proxyBeanName;
        return proxyBeanName;
    }


    /**
     * @Description: 根据类获取需要注入的字段
     **/
    public static Field[] getAutowiredField(Class<?> c) {
        List<Field> autoField = new ArrayList<>();
        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutowiredZ.class) ||
                    field.isAnnotationPresent(Resource.class)) {
                autoField.add(field);
            }
        }
        return autoField.toArray(new Field[0]);
    }

    /**
     * @params: [field]
     * @return: java.lang.String
     * @Description: 获取需注入field的key
     **/
    public static String getAutowiredValue(Field field) {
        String filedKey;
        if (field.isAnnotationPresent(AutowiredZ.class)) {
            String autowiredValue = field.getType().getSimpleName();
            //检测是否有qualifier注解，有则使用注解值来获取注入组件
            Qualifier qualifier = field.getAnnotation(Qualifier.class);
            String infuseKey = qualifier == null ? autowiredValue : qualifier.value();
            filedKey = StrUtil.firstLowerCase(infuseKey);
        } else {
            Resource resource = field.getAnnotation(Resource.class);
            String resourceValue = resource.value();
            String infuseKey = resourceValue.isEmpty() ? field.getName() : resourceValue;
            filedKey = StrUtil.firstLowerCase(infuseKey);
        }

        return filedKey;
    }

    /**
     * @Description: 从air中获取field的值
     **/
    public static Object getFieldValue(Field field, Object air) {
        try {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                return field.get(null);
            } else {
                return field.get(air);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
