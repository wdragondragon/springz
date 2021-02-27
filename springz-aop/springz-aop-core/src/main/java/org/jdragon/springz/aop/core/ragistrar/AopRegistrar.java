package org.jdragon.springz.aop.core.ragistrar;

import org.jdragon.springz.aop.annotation.*;
import org.jdragon.springz.aop.annotation.Throw;

import org.jdragon.springz.aop.core.AopContext;
import org.jdragon.springz.aop.core.entity.PointCutInfo;
import org.jdragon.springz.core.infuse.Infuser;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.core.register.Registrar;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
//        this.classInfo = classInfo;

        Class<?> clazz = classInfo.getClazz();

        if (!clazz.isAnnotationPresent(Aop.class)) return;

        Aop aop = clazz.getAnnotation(Aop.class);

        Method[] methods = clazz.getMethods();

        Method afterMethod = null;

        Method beforeMethod = null;

        Method throwableMethod = null;

        List<Pointcut> pointCuts = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(After.class)) {
                afterMethod = method;
            }
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethod = method;
            }
            if (method.isAnnotationPresent(Pointcut.class)) {
                pointCuts.add(method.getAnnotation(Pointcut.class));
            }
            if (method.isAnnotationPresent(Throw.class)) {
                throwableMethod = method;
            }
        }

        if (pointCuts.isEmpty()) {
            logger.error("aop缺少切入点", clazz.getName());
            return;
        }
        int order = aop.order();

        String definitionName = classInfo.getDefinitionName();
        BeanInfo beanInfo = beanMap.get(definitionName);

        if (beanInfo == null){
            logger.warn("切面类并未注册到容器中", definitionName);
            return;
        }

        Object bean = new Infuser().createAnalyzeBean(definitionName, beanInfo.getBean().getClass());

        for (Pointcut pointCut : pointCuts) {
            PointCutInfo pointCutInfo = new PointCutInfo(bean, pointCut, beforeMethod, afterMethod, throwableMethod, order);
            AopContext.addPointCut(pointCutInfo);
        }
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }

    @Override
    public Integer getOrder() {
        return -98;
    }
}
