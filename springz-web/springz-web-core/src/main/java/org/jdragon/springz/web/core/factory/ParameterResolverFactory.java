package org.jdragon.springz.web.core.factory;

import org.jdragon.springz.web.annotation.PathVariable;
import org.jdragon.springz.web.annotation.RequestBody;
import org.jdragon.springz.web.annotation.RequestParam;
import org.jdragon.springz.web.core.resolver.ParameterResolver;
import org.jdragon.springz.web.core.resolver.PathVariableParameterResolver;
import org.jdragon.springz.web.core.resolver.RequestBodyParameterResolver;
import org.jdragon.springz.web.core.resolver.RequestParamParameterResolver;

import java.lang.reflect.Parameter;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 14:49
 * @Description:
 */
public class ParameterResolverFactory {

    public static ParameterResolver get(Parameter parameter) {
        if (parameter.isAnnotationPresent(RequestParam.class)) {
            return new RequestParamParameterResolver();
        }
        if (parameter.isAnnotationPresent(PathVariable.class)) {
            return new PathVariableParameterResolver();
        }
        if (parameter.isAnnotationPresent(RequestBody.class)) {
            return new RequestBodyParameterResolver();
        }
        return null;
    }
}
