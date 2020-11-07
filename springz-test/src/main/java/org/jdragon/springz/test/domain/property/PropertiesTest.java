package org.jdragon.springz.test.domain.property;

import com.alibaba.fastjson.JSON;
import org.jdragon.springz.core.container.PropertiesContainer;
import org.jdragon.springz.core.annotation.Properties;
import org.jdragon.springz.core.annotation.Value;
import org.jdragon.springz.utils.StrUtil;

import java.lang.reflect.Field;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.07 18:53
 * @Description:
 */
public class PropertiesTest {
    public static void main(String[] args) {

        test(new Consumer());

        test(new ConsumerProperty());

    }

    public static void test(Object o) {
        Class<?> aClass = o.getClass();
        String prefix = "";
        String source = PropertiesContainer.DEFAULT_SOURCE;
        if (aClass.isAnnotationPresent(Properties.class)) {
            Properties properties = aClass.getAnnotation(Properties.class);
            prefix = properties.prefix();
            source = properties.source();
        }

        for (Field field : aClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Value.class)) {
                continue;
            }

            Value value = field.getAnnotation(Value.class);

            String key = StrUtil.matchWrap(value.value(), "\\$\\{", "}");

            Object defaultProperty = PropertiesContainer.getPropertyValue(prefix, key, source);

            Class<?> type = field.getType();
            Object obj = JSON.parseObject(JSON.toJSONString(defaultProperty), type);
            field.setAccessible(true);
            try {
                field.set(o, obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }

        System.out.println(o);
    }
}
