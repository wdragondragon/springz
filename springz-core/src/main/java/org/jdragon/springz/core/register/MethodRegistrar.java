package org.jdragon.springz.core.register;

import org.jdragon.springz.core.annotation.Bean;
import org.jdragon.springz.core.entry.BeanInfo;
import org.jdragon.springz.core.entry.ClassInfo;
import org.jdragon.springz.core.scan.ScanAction;
import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.02 11:27
 * @Description:
 */
public class MethodRegistrar extends Registrar implements ScanAction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MethodRegistrar(Map<String, BeanInfo> beanMap){
        super(beanMap);
    }

    @Override
    public void action(ClassInfo classInfo) {
        try {
            this.classInfo = classInfo;

            //反射构建对象
            Class<?> c = classInfo.getClazz();

            //返回的value是null，说明他所有注解和Component无关，就不需要注册
            if (getComponentValue(c) == null) {
                return;
            }

            BeanInfo obj = beanMap.get(classInfo.getDefinitionName());
            if(obj == null){
                return;
            }

            //将@Bean注解的方法，从beanMap中取出，并注入到obj中 Object->obj:beanField
            registerMethod(c.getDeclaredMethods(), obj.getBean());
        } catch (NoSuchMethodException e) {
            logger.warn("@Value注解下的变量没有String构造器", Arrays.toString(e.getStackTrace()));
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.warn("@构造失败",e.getMessage());
            e.printStackTrace();
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
            Class<?>[] methodParamTypes = method.getParameterTypes();
            List<Object> paramsList = new ArrayList<>();
            for(Class<?> methodParamType:methodParamTypes){
                String methodParamTypeName = methodParamType.getSimpleName();
                methodParamTypeName = StringUtils.firstLowerCase(methodParamTypeName);
                BeanInfo methodParamBean = beanMap.get(methodParamTypeName);
                if(methodParamBean==null){
                    logger.warn("@Bean注解下的方法参数未找到");
                    return;
                }
                paramsList.add(methodParamBean.getBean());
            }

            Object[] paramsArray = paramsList.toArray();

            Object bean = method.invoke(obj,paramsArray);

            //判断Bean注解上是否设了BeanName
            Bean beanAnnotation = method.getAnnotation(Bean.class);
            String[] beanNames = beanAnnotation.value();

            //获取Bean的模式范围
            String scope = AnnotationUtils.getScopeValue(method);

            if (beanNames.length == 0) {
                register(method.getName(), bean, scope);
                continue;
            }
            for (String beanName : beanNames) {
                register(beanName, bean, scope);
            }
        }
    }
}
