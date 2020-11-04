package org.jdragon.springz.aop.core.ragistrar;

import org.jdragon.springz.aop.annotation.*;
import org.jdragon.springz.aop.annotation.Throw;

import org.jdragon.springz.aop.core.AopContext;
import org.jdragon.springz.aop.core.entity.PointCutInfo;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.Registrar;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.27 23:42
 * @Description:
 */
public class AopRegistrar extends Registrar implements ScanAction {

    private static final Logger logger = LoggerFactory.getLogger(AopRegistrar.class);

    @Override
    public void action(ClassInfo classInfo) {
        this.classInfo = classInfo;

        Class<?> clazz = classInfo.getClazz();

        if (!clazz.isAnnotationPresent(Aop.class)) return;

        Aop aop = clazz.getAnnotation(Aop.class);

        Method[] methods = clazz.getMethods();

        Method afterMethod = null;

        Method beforeMethod = null;

        Method throwableMethod = null;

        Pointcut pointCut = null;

        for (Method method : methods) {
            if (method.isAnnotationPresent(After.class)) {
                afterMethod = method;
            }
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethod = method;
            }
            if (method.isAnnotationPresent(Pointcut.class)) {
                pointCut = method.getAnnotation(Pointcut.class);
            }
            if (method.isAnnotationPresent(Throw.class)) {
                throwableMethod = method;
            }
        }

        if (pointCut == null) {
            logger.error("aop缺少切入点", clazz.getName());
            return;
        }
        int order = aop.order();

        String definitionName = classInfo.getDefinitionName();
        BeanInfo beanInfo = beanMap.get(definitionName);

        if (beanInfo == null)
            logger.warn("切面类并未注册到容器中", definitionName);

        PointCutInfo pointCutInfo = new PointCutInfo(beanInfo.getBean(),
                pointCut, beforeMethod, afterMethod, throwableMethod, order);
        AopContext.addPointCut(pointCutInfo);
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }
}
