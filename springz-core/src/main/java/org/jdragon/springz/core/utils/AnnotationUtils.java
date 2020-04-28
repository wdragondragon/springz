package org.jdragon.springz.core.utils;


import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.core.annotation.Scope;
import org.jdragon.springz.core.entry.BeanInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 14:40
 * @Description: 注解工具类
 */
public class AnnotationUtils {

    private static final List<String> baseList = Arrays.asList("Documented","Inherited","Target","Retention");

    /**
     * @params: [annotation]
     * @return: java.lang.String
     * @Description: 获取未知注解上的value值
     **/

    public static String getAnnotationAttribute(Annotation annotation, String attribute) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<? extends Annotation> aClass = annotation.getClass();
        Method method = aClass.getDeclaredMethod(attribute);
        return (String) method.invoke(annotation);
    }

    /**
     * @params: [c]
     * @return: java.lang.String:与Component相关注解的value值
     * @Description: 判断类上有没有间接包含Component。即注解嵌套拆分
     * 如果有包含Component，那么再看有没有value值，有的话返回这个值
     **/
    public static String getComponentValue(Class<?> c) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Annotation[] annotations = c.getAnnotations();

        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            if (aClass.isAnnotationPresent(Component.class)||isIncludeComponent(c)) {
                return getAnnotationAttribute(annotation, "value");
            }
        }
        return null;
    }

    public static boolean isIncludeComponent(Class<?> c){
        Annotation[] annotations = c.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            if (aClass.isAnnotationPresent(Component.class)) {
                return true;
            }else if(!baseList.contains(aClass.getSimpleName())){
                return isIncludeComponent(aClass);
            }
        }
        return false;
    }
    /**
     * @params: [c]
     * @return: java.lang.String:与Component相关注解的value值
     * @Description: 判断类或方法上有没有Scope
     * 如果有包含Scope，那么再看有没有value值，没有的话返回默认的SINGLETON
     **/
    public static String getScopeValue(AnnotatedElement c) {
        String scopeValue = null;
        if(c.isAnnotationPresent(Scope.class)){
            Scope scope = c.getAnnotation(Scope.class);
            scopeValue = scope.value();
        }
        if(scopeValue==null||scopeValue.isEmpty()){
            scopeValue = BeanInfo.SINGLETON;
        }
        return scopeValue;
    }
    /**
     * @params: [element]
     * @return: java.lang.String
     * @Description:
     **/
    public static String checkIncludeQualifier(AnnotatedElement element){

        //检测是否有qualifier注解，有则使用注解值来获取注入组件
        String qualifierValue = null;
        if (element.isAnnotationPresent(Qualifier.class)) {
            Qualifier qualifier = element.getDeclaredAnnotation(Qualifier.class);
            qualifierValue =  qualifier.value();
        }
        return qualifierValue;
    }
}
