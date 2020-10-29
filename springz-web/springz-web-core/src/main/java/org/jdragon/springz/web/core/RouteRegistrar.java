package org.jdragon.springz.web.core;

import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.Registrar;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.web.annotation.RequestMapping;
import org.jdragon.springz.web.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.27 20:43
 * @Description:
 */
public class RouteRegistrar extends Registrar implements ScanAction {

    @Override
    public void action(ClassInfo classInfo) {
        this.classInfo = classInfo;
        Class<?> clazz = classInfo.getClazz();

        if (!clazz.isAnnotationPresent(RequestMapping.class)) return;

        RequestMapping classMapping = clazz.getAnnotation(RequestMapping.class);
        RequestMethod classMethod = classMapping.method();
        String classPath = classMapping.value();
        for (Method clazzMethod : clazz.getMethods()) {
            RequestMapping methodMapping = (RequestMapping) AnnotationUtils.getAllContainedAnnotationType(clazzMethod, RequestMapping.class);

        }
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }
}
