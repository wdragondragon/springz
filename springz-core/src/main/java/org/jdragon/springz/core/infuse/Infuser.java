package org.jdragon.springz.core.infuse;


import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Resource;

import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Bean2Utils;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.StringUtils;


import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 17:43
 * @Description: 注入者
 */
public class Infuser implements ScanAction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, BeanInfo> beanMap;

    private final Filter[] filters;

    public Infuser(Map<String, BeanInfo> beanMap,Filter...filters) {
        this.beanMap = beanMap;
        this.filters = filters;
    }

    /**
     * @Author: Jdragon
     * @Date: 2020.04.25 下午 8:15
     * @params: [beanInfo]
     * @return: void
     * @Description: 根据传入的beanInfo来获取已注册bean通过反射给注解下的字段赋值
     **/
    @Override
    public void action(ClassInfo classInfo) {
        try {
            String definitionName = classInfo.getDefinitionName();

            //反射构建对象
            Class<?> c = classInfo.getClazz();

            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                //只有field中有Autowired才继续
                if (field.isAnnotationPresent(AutowiredZ.class)) {
                    this.infuse(definitionName, field, getAutowiredValue(field));
                } else if (field.isAnnotationPresent(Resource.class)) {
                    this.infuse(definitionName,field,getResourceValue(field));
                }
            }

            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                //只有method中有Autowired或Resource才继续，并且优先使用Autowired
                Annotation annotation = method.getAnnotation(AutowiredZ.class);
                annotation = annotation != null ? annotation : method.getAnnotation(Resource.class);
                if (annotation == null) {
                    continue;
                }

                String methodName = method.getName();

                if (methodName.indexOf("set") != 0) {
                    throw new NoSuchMethodException(methodName);
                }

                String fieldName = methodName.replaceAll("set", "");
                fieldName = StringUtils.firstLowerCase(fieldName);

                Field field = c.getDeclaredField(fieldName);

                if (field == null) {
                    throw new NoSuchFieldException(fieldName);
                }

                if (annotation instanceof AutowiredZ) {
                    this.infuse(definitionName, field, getAutowiredValue(field));
                } else {
                    this.infuse(definitionName, field, getResourceValue(field));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            logger.error("缺失空参构造器");
        } catch (NoSuchFieldException e) {
            logger.error("注入方法下的字段不存在", e.getMessage());
        } catch (NoSuchMethodException e) {
            logger.error("注入方法必须为setXXX", e.getMessage());
        }
    }

    @Override
    public Filter[] getFilters() {
        return filters;
    }

    /**
     * @params: [field]
     * @return: java.lang.String
     * @Description: 根据field获取注入对象的key
     **/
    public String getAutowiredValue(Field field) {
        Class<?> fieldClass = field.getType();

        String autowiredValue = fieldClass.getSimpleName();
        //检测是否有qualifier注解，有则使用注解值来获取注入组件
        String qualifierValue = AnnotationUtils.checkIncludeQualifier(field);
        String infuseKey = qualifierValue == null ? autowiredValue : qualifierValue;

        return StringUtils.firstLowerCase(infuseKey);
    }

    /**
     * @params: [field]
     * @return: java.lang.String
     * @Description: 根据field获取注入对象的key
     **/
    public String getResourceValue(Field field) {
        Resource resource = field.getAnnotation(Resource.class);
        String resourceValue = resource.value();
        String infuseKey = resourceValue.isEmpty() ? field.getName() : resourceValue;
        return StringUtils.firstLowerCase(infuseKey);
    }

    /**
     * @params: [field, targetKey, objectKey]
     * @return: void
     * @Description: 传入注入目标的targetKey，目标内需注入的field，注入对象的objectKey
     * 从beanMap中获取targetObject和object，将object注入到targetObject的field中
     **/
    public void infuse(String targetKey, Field field, String objectKey) throws IllegalAccessException, InstantiationException {
        if(!beanMap.containsKey(objectKey)){
            logger.warn("注入Bean失败","找不到ObjKey",objectKey);
            return;
        }
        BeanInfo iBeanInfo = beanMap.get(objectKey);
        Object iBean;
        if (iBeanInfo.getScope().equals(BeanInfo.SINGLETON)) {
            iBean = iBeanInfo.getBean();
        } else {
            Object oldBean = iBeanInfo.getBean();
            iBean = Bean2Utils.copy(oldBean);
        }

        field.setAccessible(true);
        //是否为静态字段
        if (Modifier.isStatic(field.getModifiers())) {
            field.set(null, iBean);
        } else {
            field.set(beanMap.get(targetKey).getBean(), iBean);
        }

        logger.info("注入对象成功", targetKey, objectKey);
    }
}
