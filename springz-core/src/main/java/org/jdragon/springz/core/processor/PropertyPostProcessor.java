package org.jdragon.springz.core.processor;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.jdragon.springz.core.annotation.Properties;
import org.jdragon.springz.core.container.PropertiesContainer;
import org.jdragon.springz.core.entry.PostAutowiredBean;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.lang.reflect.Field;
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

    @SneakyThrows
    @Override
    public PostAutowiredBean postProcessAfterInitialization(PostAutowiredBean postAutowiredBean) {
        BeanInfo beanInfo = postAutowiredBean.getBeanInfo();
        Object lastBean = postAutowiredBean.getLastBean();
        Class<?> clazz = beanInfo.getClazz();
        String definedName = beanInfo.getDefinedName();

        if (propertyMap.containsKey(definedName)) {
            Properties properties = propertyMap.get(definedName);
            String prefix = properties.prefix();
            String source = properties.source();

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                String valueKey = field.getName();
                Object propertyValue = PropertiesContainer.getPropertyValue(prefix, valueKey, source);
                Object valueObj = JSON.parseObject(JSON.toJSONString(propertyValue), field.getType());
                if (valueObj == null) continue;
                field.setAccessible(true);
                field.set(lastBean, valueObj);
                logger.info("注入默认属性成功[类名][字段]", clazz.getName(), field.getName(), valueObj.toString());
            }
        }

        return postAutowiredBean;
    }

    public static void regPostProperties(String definedName, Properties properties) {
        propertyMap.put(definedName, properties);
    }
}
