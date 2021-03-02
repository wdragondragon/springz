package org.jdragon.springz.core.utils;

import org.jdragon.springz.core.annotation.Destroy;
import org.jdragon.springz.core.container.PostProcessorContainer;
import org.jdragon.springz.core.entry.PostAutowiredBean;
import org.jdragon.springz.scanner.BeanContainer;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.InjectedObject;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 12:05
 * @Description: 后置处理器执行者
 */
public class PostExecutor {

    private static final Logger logger = LoggerFactory.getLogger(PostExecutor.class);

    //已经初始化过的链表，以类名为索引
    private static final List<String> postConstruct = new LinkedList<>();

    public static Object postConstruct(Object bean, BeanInfo beanInfo) {
        //判断是否需要执行 构建完成的postConstruct方法
        PostAutowiredBean postAutowiredBean = new PostAutowiredBean(beanInfo, bean);
        bean = PostProcessorContainer.invokePostProcessor(postAutowiredBean);//后置处理
        if (needPostConstruct(beanInfo)) {
            Class<?> aClass = bean.getClass();
            // cglib 多级代理处理
            if (aClass.getName().contains("$$")) {
                aClass = aClass.getSuperclass();
            }

            InjectedObject injectedObject = new InjectedObject(bean, aClass);

            for (Method method : aClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    method.setAccessible(true);
                    try {
                        method.invoke(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (method.isAnnotationPresent(Destroy.class)) {
                    injectedObject.addDestroyMethod(method);
                }
            }
            //如果是单例，只需要执行一次
            if (beanInfo.isSingleton()) {
                postConstruct.add(beanInfo.getClassName());
            }
            BeanContainer.getInjectedObjectList().add(injectedObject);
        }
        return bean;
    }

    private static boolean needPostConstruct(BeanInfo beanInfo) {
        return beanInfo.isPrototype() ||
                (beanInfo.isSingleton() && !postConstruct.contains(beanInfo.getClassName()));
    }
}
