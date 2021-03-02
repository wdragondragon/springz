package org.jdragon.springz.core.hook;

import org.jdragon.springz.scanner.BeanContainer;
import org.jdragon.springz.scanner.entry.InjectedObject;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author JDragon
 * @Date 2021.03.02 上午 12:12
 * @Email 1061917196@qq.com
 * @Des:
 */
public class DestroyHook {

    private static final Logger logger = LoggerFactory.getLogger(DestroyHook.class);

    public static void register() {
        Thread t = new Thread(() -> {
            List<InjectedObject> injectedObjectList = BeanContainer.getInjectedObjectList();
            logger.debug("程序即将终止...");
            logger.debug("正在执行销毁动作...");
            for (InjectedObject injectedObject : injectedObjectList) {
                List<Method> destroyMethods = injectedObject.getDestroyMethods();
                for (Method destroyMethod : destroyMethods) {
                    try {
                        destroyMethod.invoke(injectedObject.getBean(), (Object[]) null);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            logger.debug("程序已完成销毁动作...");
        });
        Runtime.getRuntime().addShutdownHook(t);
    }
}
