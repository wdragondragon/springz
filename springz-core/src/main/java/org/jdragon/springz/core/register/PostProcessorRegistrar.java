package org.jdragon.springz.core.register;

import org.jdragon.springz.core.postProcessor.BeanPostProcessor;
import org.jdragon.springz.core.postProcessor.PostProcessorContext;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.Registrar;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 11:21
 * @Description:
 */
public class PostProcessorRegistrar extends Registrar implements ScanAction {
    private static final Logger logger = LoggerFactory.getLogger(PostProcessorRegistrar.class);

    @Override
    public void action(ClassInfo classInfo) {
        //判断是否为BeanPostProcessor的子类实现
        Class<?> clazz = classInfo.getClazz();
        if (!BeanPostProcessor.class.isAssignableFrom(clazz)) return;
        try {
            Constructor constructor = clazz.getConstructor();
            BeanPostProcessor BeanPostProcessor = (BeanPostProcessor) constructor.newInstance();
            PostProcessorContext.registerBeanPostProcessor(BeanPostProcessor);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }
}
