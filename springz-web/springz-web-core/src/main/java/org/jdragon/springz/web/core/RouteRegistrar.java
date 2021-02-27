package org.jdragon.springz.web.core;

import org.jdragon.springz.core.infuse.Infuser;
import org.jdragon.springz.core.register.Registrar;
import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.core.utils.BeanHelper;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.web.annotation.RequestBody;
import org.jdragon.springz.web.annotation.RequestMapping;
import org.jdragon.springz.web.annotation.RequestMethod;
import org.jdragon.springz.web.annotation.ResponseBody;
import org.jdragon.springz.web.core.entity.RouteInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.27 20:43
 * @Description:
 */
public class RouteRegistrar extends Registrar implements ScanAction {

    @Override
    public void action(ClassInfo classInfo) {
//        this.classInfo = classInfo;
        Class<?> clazz = classInfo.getClazz();

        if (!clazz.isAnnotationPresent(RequestMapping.class)) return;

        String proxyBeanName = BeanHelper.getClassBeanName(clazz);
        if (!beanMap.containsKey(proxyBeanName)) return;
        Object bean = new Infuser().createAnalyzeBean(proxyBeanName, clazz);

        RequestMapping classMapping = clazz.getAnnotation(RequestMapping.class);
        boolean classIncludeResponseBody = AnnotationUtils.isIncludeAnnotationType(clazz, ResponseBody.class);

        String classUrl = classMapping.value();
        for (Method clazzMethod : clazz.getMethods()) {
            RequestMapping requestMapping;
            String methodUrl;
            if (clazzMethod.isAnnotationPresent(RequestMapping.class)) {
                requestMapping = clazzMethod.getAnnotation(RequestMapping.class);
                methodUrl = requestMapping.value();
            } else if (AnnotationUtils.isIncludeAnnotationType(clazzMethod, RequestMapping.class)) {
                requestMapping = (RequestMapping) AnnotationUtils.getAllContainedAnnotationType(clazzMethod, RequestMapping.class);
                Annotation requestTypeMapping = AnnotationUtils.getIncludeAnnotationType(clazzMethod, RequestMapping.class);
                methodUrl = (String) AnnotationUtils.getAnnotationAttribute(Objects.requireNonNull(requestTypeMapping), "value");
            } else {
                continue;
            }

            //是否包含requestBody
            boolean methodIncludeResponseBody = classIncludeResponseBody || clazzMethod.isAnnotationPresent(ResponseBody.class);

            RequestMethod[] requestMethods = requestMapping.method();

            Parameter[] parameters = clazzMethod.getParameters();

            RouteInfo routeInfo = RouteInfo.builder()
                    .requestUrl(classUrl + methodUrl)
                    .bindBeanName(proxyBeanName)
                    .bindObj(bean)
                    .bindMethod(clazzMethod)
                    .requestMethod(requestMethods)
                    .invokeParams(parameters)
                    .useDataMode(methodIncludeResponseBody)
                    .build();

            RouteMethodMapper.registrar(routeInfo);
        }
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }
}
