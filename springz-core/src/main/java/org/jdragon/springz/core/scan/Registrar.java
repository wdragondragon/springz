package org.jdragon.springz.core.scan;

import org.jdragon.springz.core.annotation.Bean;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.Value;
import org.jdragon.springz.core.entry.BeanInfo;
import org.jdragon.springz.core.entry.ClassInfo;
import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.utils.LogBuilder;
import org.jdragon.springz.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 15:37
 * @Description: 注册者
 */

public class Registrar implements ScanAction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, BeanInfo> beanMap = new HashMap<>();

    private ClassInfo classInfo;

    /**
     * @params: [beanInfo]
     * @return: void
     * @Description: 根据传入的beanInfo生成类实例并放入beanMap中
     **/
    @Override
    public void action(ClassInfo classInfo) {
        try {

            this.classInfo = classInfo;

            //反射构建对象
            Class<?> c = classInfo.getClazz();
            //判断类是否为注解，如果是则不需要注册
            if (c.isAnnotation()) {
                return;
            }

            //从类中检测是否有Component注解
            //若无，再检查类中所有的注解是否有包含Component注解（注解嵌套）
            String value;
            if (c.isAnnotationPresent(Component.class)) {
                value = AnnotationUtils.getAnnotationAttribute(c.getAnnotation(Component.class), "value");
            } else {
                value = AnnotationUtils.checkIncludeComponent(c);
            }

            //如果上面返回的value是null，说明他所有注解和Component无关，就不需要注册
            if (value == null) {
                return;
            }

            //获取Bean的模式范围
            String scopeValue = AnnotationUtils.checkIncludeScope(c);

            //声明注册的对象
            Object obj = c.newInstance();

            //如果返回非空串，说明有value属性
            //如果返回空串，说明虽然有注解，但没有设置value属性
            //因为value值是用来解决键名冲突的
            //所以在没有设置value属性值的时候才进行接口名注册，否则使用value值注册
            if (value.isEmpty()) {
                //把实现接口放到map容器 beanMap->InterfacesName:obj
                registerInterfaces(c.getInterfaces(), obj,scopeValue);
            }else{
                value = StringUtils.firstLowerCase(value);
                register(value, obj,scopeValue);
            }
            //将对象放到map容器 beanMap->definitionName:obj
            register(classInfo.getDefinitionName(), obj,scopeValue);

            //将@Value注册到对象中 Object->obj:baseField
            registerFields(c.getDeclaredFields(), obj);

            //将@Bean注解的方法，从beanMap中取出，并注入到obj中 Object->obj:beanField
            registerMethod(c.getDeclaredMethods(), obj);

        } catch (NoSuchMethodException e) {
            logger.warn(LogBuilder.build("@Value注解下的变量没有String构造器"),e.getCause());
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            logger.warn(LogBuilder.build("@构造失败"),e.getCause());
            e.printStackTrace();
        }
    }

    /**
     * @params: [definitionName, obj]
     * @return: void
     * @Description: 通用注册类，将definitionName作为key,obj作为value存到beanMap中
     **/
    private void register(String definitionName, Object obj,String scope) {
        //检查definitionName是否存在
        if (beanMap.containsKey(definitionName)) {
            Object existObj = beanMap.get(definitionName);
            logger.warn(LogBuilder.build("已存在键名",definitionName,existObj.getClass().getName()));
            logger.warn(LogBuilder.build("请解决类键名冲突",definitionName, classInfo.getClassName()));
            return;
        }
        //将对象放到map容器
        beanMap.put(definitionName, new BeanInfo(obj,scope));
        if (beanMap.containsKey(definitionName)) {
            logger.info(LogBuilder.build("注册bean成功",definitionName, classInfo.getClassName()));
        }
    }

    /**
     * @params: [interfaces, obj]
     * @return: void
     * @Description: 从传入的接口中，对接口的名称小写后注册到beanMap中
     **/
    private void registerInterfaces(Class<?>[] interfaces, Object obj,String scope) {
        for (Class<?> anInterface : interfaces) {
            String interfaceName = StringUtils.firstLowerCase(anInterface.getSimpleName());
            //这里检测到的话代表有多个接口实现类，需要将接口组件注销
            register(interfaceName, obj, scope);
        }
    }

    /**
     * @params: [fields, obj]
     * @return: void
     * @Description: 从传入的字段中，对字段判断是否有@Value，有则获取注解的值来注册bean到beanMap中
     **/
    private void registerFields(Field[] fields, Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Value.class)) {
                continue;
            }
            Value valueAnnotation = field.getAnnotation(Value.class);
            String value = valueAnnotation.value();
            Constructor<?> constructor = field.getType().getConstructor(String.class);
            field.setAccessible(true);
            field.set(obj, constructor.newInstance(value));
            logger.info(LogBuilder.build("注入默认属性成功", classInfo.getClassName(),value));
        }
    }

    /**
     * @params: [methods, obj]
     * @return: void
     * @Description: 从传入的方法中，对方法判断是否有@Bean，有则注册Bean到beanMap中
     **/
    private void registerMethod(Method[] methods, Object obj) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            //判断是否有Bean注解
            if (!method.isAnnotationPresent(Bean.class)) {
                continue;
            }
            //根据obj中的方法获取method上的@Bean注解的方法返回值
            Object bean = method.invoke(obj);

            //判断Bean注解上是否设了BeanName
            Bean beanAnnotation = method.getAnnotation(Bean.class);
            String[] beanNames = beanAnnotation.value();

            //获取Bean的模式范围
            String scope = AnnotationUtils.checkIncludeScope(method);

            if (beanNames.length == 0) {
                register(method.getName(), bean, scope);
                continue;
            }
            for (String beanName : beanNames) {
                register(beanName, bean, scope);
            }
        }
    }

    public Map<String, BeanInfo> getBeanOfAll() {
        return beanMap;
    }

}