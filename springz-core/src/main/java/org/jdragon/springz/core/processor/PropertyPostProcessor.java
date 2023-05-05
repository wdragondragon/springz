package org.jdragon.springz.core.processor;

import com.alibaba.fastjson.JSON;
import org.jdragon.springz.core.annotation.Properties;
import org.jdragon.springz.core.annotation.Value;
import org.jdragon.springz.core.container.PropertiesContainer;
import org.jdragon.springz.core.entry.PostAutowiredBean;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.StrUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author JDragon
 * @Date 2021.02.28 下午 11:56
 * @Email 1061917196@qq.com
 * @Des: 后置注入@Bean下的@Properties
 */
public class PropertyPostProcessor implements BeanPostProcessor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 用来放置需要后置注入property的 definedName和propertis的映射
     */
    private static Map<String, Properties> propertyMap = new HashMap<>(8);

    @Override
    public PostAutowiredBean postProcessAfterInitialization(PostAutowiredBean postAutowiredBean) {
        BeanInfo beanInfo = postAutowiredBean.getBeanInfo();
        Object lastBean = postAutowiredBean.getLastBean();
        Class<?> clazz = beanInfo.getClazz();
        String definedName = beanInfo.getDefinedName();

        try {
            if (propertyMap.containsKey(definedName)) {
                Properties properties = propertyMap.get(definedName);
                String prefix = properties.prefix();
                String source = properties.source();

                Field[] fields = clazz.getDeclaredFields();

                for (Field field : fields) {
                    String valueKey;
                    Object valueObj = null;
                    if (field.isAnnotationPresent(Value.class)) {
                        Value valueAnnotation = field.getAnnotation(Value.class);
                        String value = valueAnnotation.value();
                        valueKey = StrUtil.matchWrap(value, "\\$\\{", "}");
                        if (valueKey.isEmpty()) {
                            Constructor<?> constructor = field.getType().getConstructor(String.class);
                            valueObj = constructor.newInstance(value);
                        }
                    } else {
                        valueKey = field.getName();
                    }

                    //空则表示不是通过@Value使用string注入，而是需要通过配置注入
                    if (valueObj == null) {
                        Object propertyValue = PropertiesContainer.getPropertyValue(prefix, valueKey, source);
                        valueObj = JSON.parseObject(JSON.toJSONString(propertyValue), field.getType());
                    }

                    if (valueObj == null) continue;
                    field.setAccessible(true);
                    field.set(lastBean, valueObj);
                    logger.info("注入默认属性成功[类名][字段]", clazz.getName(), field.getName(), valueObj.toString());
                }
            }
        }catch (NoSuchMethodException e){
            logger.warn("@Value注解下的变量没有String构造器", Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.warn("@构造失败", e.getCause().toString());
            e.printStackTrace();
        }

        return postAutowiredBean;
    }

    public static void regPostProperties(String definedName, Properties properties) {
        propertyMap.put(definedName, properties);
    }
}
