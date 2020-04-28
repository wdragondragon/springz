package org.jdragon.springz.core.scan;

import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.core.annotation.Resource;
import org.jdragon.springz.core.entry.BeanInfo;
import org.jdragon.springz.core.entry.ClassInfo;
import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.utils.Bean2Utils;
import org.jdragon.springz.utils.LogBuilder;
import org.jdragon.springz.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Map<String, BeanInfo> beanMap;

    public Infuser(Map<String, BeanInfo> beanMap) {
        this.beanMap = beanMap;
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
                if (field.isAnnotationPresent(Autowired.class)) {
                    this.infuseAutowired(field, definitionName, getInfuseKey(field));
                } else if (field.isAnnotationPresent(Resource.class)) {
                    this.infuseResource();
                }
            }

            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                //只有method中有Autowired才继续
                if (method.isAnnotationPresent(Autowired.class)) {
                    String methodName = method.getName();

                    if(methodName.indexOf("set")!=0){
                        throw new NoSuchMethodException(methodName);
                    }

                    String fieldName = methodName.replaceAll("set", "");

                    fieldName = StringUtils.firstLowerCase(fieldName);

                    Field field = c.getDeclaredField(fieldName);

                    if(field==null){
                        throw new NoSuchFieldException(fieldName);
                    }

                    this.infuseAutowired(field, definitionName, getInfuseKey(field));
                } else if (method.isAnnotationPresent(Resource.class)) {
                    this.infuseResource();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            logger.error(LogBuilder.build("缺失空参构造器"));
        } catch (NoSuchFieldException e) {
            logger.error(LogBuilder.build("注入方法下的字段不存在",e.getMessage()));
        } catch (NoSuchMethodException e){
            logger.error(LogBuilder.build("注入方法必须为setXXX",e.getMessage()));
        }
    }

    /**
     * @params: [field]
     * @return: java.lang.String
     * @Description: 根据field获取注入对象的key
     **/
    public String getInfuseKey(Field field) {
        Class<?> fieldClass = field.getType();

        String autowiredValue = fieldClass.getSimpleName();
        //检测是否有qualifier注解，有则使用注解值来获取注入组件
        String qualifierValue = AnnotationUtils.checkIncludeQualifier(field);
        String infuseKey = qualifierValue == null ? autowiredValue : qualifierValue;

        return StringUtils.firstLowerCase(infuseKey);
    }

    /**
     * @params: [field, targetKey, objectKey]
     * @return: void
     * @Description: 获取注入目标的targetKey和目标内需注入的field
     * 获取targetKey对应的bean，将objectKey注入到target的field中
     **/
    public void infuseAutowired(Field field, String targetKey, String objectKey) throws IllegalAccessException, InstantiationException {
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

        logger.info(LogBuilder.build("注入对象成功", targetKey, objectKey));
    }

    public void infuseResource() {
        logger.info("resource");
    }
}
