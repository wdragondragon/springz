package org.jdragon.springz.core.register;


import org.jdragon.springz.core.annotation.Bean;
import org.jdragon.springz.core.annotation.Inject;
import org.jdragon.springz.core.annotation.Properties;
import org.jdragon.springz.core.processor.PropertyPostProcessor;
import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.core.utils.BeanHelper;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.scanner.entry.WaitBeanInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.StrUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.05.02 11:27
 * @Description: 方法注册器
 */
public class MethodComponentRegistrar extends Registrar implements ScanAction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Filter[] filters;

    public MethodComponentRegistrar(Filter... filters) {
        this.filters = filters;
        logger.info("加载注册器", "MethodComponentRegistrar");
    }

    @Override
    public void action(ClassInfo classInfo) {
        try {

            //反射构建对象
            Class<?> clazz = classInfo.getClazz();

            //返回的value是null，说明他所有注解和Component无关，就不需要注册
            if (BeanHelper.getComponentValue(clazz) == null) {
                return;
            }

            BeanInfo obj = beanMap.get(classInfo.getDefinitionName());
            if (obj == null) {
                return;
            }

            //将@Bean注解的方法，从beanMap中取出，并注入到obj中 Object->obj:beanField
            registerMethod(clazz.getDeclaredMethods(), obj.getBean());

        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.warn("@构造失败", e.getMessage());
            e.printStackTrace();
        } catch (NoSuchMethodException | InstantiationException e) {
            logger.warn("registerMethod", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Filter[] getFilters() {
        return filters;
    }

    /**
     * @params: [methods, obj]
     * @return: void
     * @Description: 从传入的方法中，对方法判断是否有@Bean，有则注册Bean到beanMap中
     **/
    private void registerMethod(Method[] methods, Object obj) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        for (Method method : methods) {
            //判断是否有Bean注解
            if (!method.isAnnotationPresent(Bean.class)) {
                continue;
            }
            //根据obj中的方法获取method上的@Bean注解的方法返回值
            Class<?>[] methodParamTypes = method.getParameterTypes();
            //获取Bean的模式范围
            String scope = AnnotationUtils.getScopeValue(method);

            //判断Bean注解上是否设了BeanName
            Bean beanAnnotation = method.getAnnotation(Bean.class);

            String beanName = beanAnnotation.value();
            if (beanName.isEmpty()) {
                beanName = method.getName();
            }

            //先把方法需要的beanName收集
            List<String> paramsNameList = collectMethodNeedBeanName(methodParamTypes);
            //再用收集来的beanName来对beanMpa查找，若所需的bean都存在则构造，
            //否则就加入needBeanName中放入waitBeanMap中，等Registrar待唤醒构造
            List<Object> paramsList = new ArrayList<>();
            List<String> needBeanName = new ArrayList<>();
            findAndAddBean(beanName, paramsNameList, paramsList, needBeanName);

            //若存在needBean的话，结束该bean注册。等待其他Registrar的唤醒
            if (!needBeanName.isEmpty()) {
                super.addWaitBean(new WaitBeanInfo(beanName, method.getReturnType(),
                        obj, method, scope, paramsNameList, needBeanName));
                continue;
            }
            //因为存在延迟加载，method上的properties选择后置处理
            if (method.isAnnotationPresent(Properties.class)) {
                Properties properties = method.getAnnotation(Properties.class);
                PropertyPostProcessor.regPostProperties(beanName, properties);
            }

            Object[] paramsArray = paramsList.toArray();

            Object bean = method.invoke(obj, paramsArray);

            register(method.getReturnType(), beanName, bean, scope);
        }
    }

    private void findAndAddBean(String beanName, List<String> paramsNameList, List<Object> paramsList, List<String> needBeanName) {
        for (String paramName : paramsNameList) {
            BeanInfo methodParamBean = beanMap.get(paramName);
            if (methodParamBean == null) {
                logger.warn("@Bean注解下的方法参数未找到，加入待唤醒队列", beanName, "缺少" + paramName);
                needBeanName.add(paramName);
            } else {
                paramsList.add(methodParamBean.getBean());
            }
        }
    }

    private List<String> collectMethodNeedBeanName(Class<?>[] methodParamTypes) {
        List<String> paramsNameList = new ArrayList<>();
        //先把方法需要的beanName收集
        for (Class<?> methodParamType : methodParamTypes) {
            String methodParamTypeName = "";
            if (methodParamType.isAnnotationPresent(Inject.class)) {
                methodParamTypeName = methodParamType.getAnnotation(Inject.class).value();
            }
            if (StrUtil.isBlank(methodParamTypeName)) {
                methodParamTypeName = methodParamType.getSimpleName();
            }
            methodParamTypeName = StrUtil.firstLowerCase(methodParamTypeName);
            paramsNameList.add(methodParamTypeName);
        }
        return paramsNameList;
    }

    private void propertiesMethod() {

    }

    @Override
    public Integer getOrder() {
        return -90;
    }
}
