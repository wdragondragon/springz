package org.jdragon.springz.core.register;


import org.jdragon.springz.core.annotation.Import;
import org.jdragon.springz.core.annotation.Value;
import org.jdragon.springz.scanner.entry.BeanInfo;

import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 15:37
 * @Description: 类注册器
 */

public class TypeComponentRegistrar extends ComponentRegistrar implements ScanAction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    Filter[] filters;

    public TypeComponentRegistrar(Filter... filters) {
//        super(beanMap);
        this.filters = filters;
        logger.info("加载注册器", "TypeComponentRegistrar");
    }

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

            String value = getComponentValue(c);
            //返回的value是null，说明他所有注解和Component无关，就不需要注册
            if (value == null) {
                return;
            }

            //获取Bean的模式范围
            String scopeValue = AnnotationUtils.getScopeValue(c);

            //声明注册的对象
            Object obj = c.newInstance();

            //如果返回非空串，说明有value属性
            //如果返回空串，说明虽然有注解，但没有设置value属性
            //因为value值是用来解决键名冲突的
            //所以在没有设置value属性值的时候才进行接口名注册，否则使用value值注册
            if (value.isEmpty()) {
                //把实现接口放到map容器 beanMap->InterfacesName:obj
                registerInterfaces(c.getInterfaces(), obj, scopeValue);
            } else {
                value = StringUtils.firstLowerCase(value);
                register(value, obj, scopeValue);
            }
            //将对象放到map容器 beanMap->definitionName:obj
            register(classInfo.getDefinitionName(), obj, scopeValue);

            //将@Import注解中的类根据全类名注册到beanMap->className:class.getInstance
            registerImport(c);

            //将@Value注册到对象中 Object->obj:baseField
            registerFields(c.getDeclaredFields(), obj);

        } catch (NoSuchMethodException e) {
            logger.warn("@Value注解下的变量没有String构造器", Arrays.toString(e.getStackTrace()));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            logger.warn("@构造失败", e.getCause().toString());
            e.printStackTrace();
        }
    }

    @Override
    public Filter[] getFilters() {
        return filters;
    }

    /**
     * @params: [c, scopeValue]
     * @return: void
     * @Description: 注册import注解
     **/
    private void registerImport(Class<?> c) throws IllegalAccessException, InstantiationException {
        if (!c.isAnnotationPresent(Import.class)) {
            return;
        }
        Import importAnnotation = c.getAnnotation(Import.class);
        Class<?>[] importClasses = importAnnotation.value();
        for (Class<?> importClass : importClasses) {
            beanMap.put(StringUtils.firstLowerCase(importClass.getSimpleName()),
                    new BeanInfo(importClass.newInstance(), importClass.getName()));
        }
    }

    /**
     * @params: [interfaces, obj]
     * @return: void
     * @Description: 从传入的接口中，对接口的名称小写后注册到beanMap中
     **/
    private void registerInterfaces(Class<?>[] interfaces, Object obj, String scope) {
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
            logger.info("注入默认属性成功", classInfo.getClassName(), value);
        }
    }
}