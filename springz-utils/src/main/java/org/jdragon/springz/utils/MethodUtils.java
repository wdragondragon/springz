package org.jdragon.springz.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.11 13:06
 * @Description:
 */
public class MethodUtils {
    public static Object invoke(Object obj, Method method, Object...params){
        try {
            return method.invoke(obj, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
