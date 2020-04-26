package org.jdragon.springz.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 19:52
 * @Description: Bean工具
 */
public class Bean2Utils {
    /**
     * @params: [source]
     * @return: java.lang.Object
     * @Description: 进行类的深拷贝
     **/
    public static Object copy(Object source) throws IllegalAccessException, InstantiationException {

        Class<?> clazz = source.getClass();
        Object o = clazz.newInstance();
        while (clazz != Object.class) {

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(source);
                // 基本类型
                if (field.getType().isPrimitive()) {
                    field.set(o, value);
                }
                // 数组类型  因为数组类型也是 Object 的实例, 所以写在前面
                else if (field.getType().isArray()) {
                    field.set(o, operateArray(value));
                }
                // 不为null的对象
                else if (value != null) {
                    field.set(o, copy(value));
                }
                field.setAccessible(false);
            }
            clazz = clazz.getSuperclass();
        }
        return o;
    }


    private static Object operateArray(Object array) throws InstantiationException, IllegalAccessException {
        // 1. 数组不为null, 2. 数组长度 >= 0
        if (array == null) {
            return null;
        }

        int length = Array.getLength(array);
        Class<?> componentType = array.getClass().getComponentType();

        // 基本类型 + String 类型, 因为String 类型的值是不变的, 所以采用等值
        if (componentType.isPrimitive() || componentType == String.class) {
            return returnPrimitive(array, length);
        }
        // 保证长度 > 0
        if (componentType.isArray()) {
            // 固定长度的多维数组
            Object value = Array.newInstance(array.getClass().getComponentType(), Array.getLength(array));
            int len = Array.getLength(array);
            for (int i = 0; i < len; i++) {
                Array.set(value, i, operateArray(Array.get(array, i)));
            }
            return value;
        } else {
            Object o = Array.newInstance(componentType, length);
            for (int i = 0; i < length; i++) {
                Object value = copy(Array.get(array, i));
                Array.set(o, i, value);
            }
            return o;
        }
    }

    private static Object returnPrimitive(Object array, int length) {
        Class<?> componentType = array.getClass().getComponentType();
        if (componentType == int.class) {
            return Arrays.copyOf((int[]) array, length);
        } else if (componentType == double.class) {
            return Arrays.copyOf((double[]) array, length);
        } else if (componentType == float.class) {
            return Arrays.copyOf((float[]) array, length);
        } else if (componentType == long.class) {
            return Arrays.copyOf((int[]) array, length);
        } else if (componentType == boolean.class) {
            return Arrays.copyOf((boolean[]) array, length);
        } else if (componentType == byte.class) {
            return Arrays.copyOf((byte[]) array, length);
        } else if (componentType == short.class) {
            return Arrays.copyOf((short[]) array, length);
        } else if (componentType == char.class) {
            return Arrays.copyOf((char[]) array, length);
        } else if (componentType == String.class) {
            return Arrays.copyOf((String[]) array, length);
        }
        return null;
    }
}
