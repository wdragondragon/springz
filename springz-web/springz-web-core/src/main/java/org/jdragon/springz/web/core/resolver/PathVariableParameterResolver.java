package org.jdragon.springz.web.core.resolver;

import org.jdragon.springz.utils.json.JsonUtils;
import org.jdragon.springz.web.annotation.PathVariable;
import org.jdragon.springz.web.core.entity.MethodParam;

import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 14:26
 * @Description:
 */
public class PathVariableParameterResolver implements ParameterResolver {
    @Override
    public Object resolve(MethodParam methodParam, Parameter parameter) {
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        String pathParameter = pathVariable.value();
        if (pathParameter.isEmpty()) {
            pathParameter = parameter.getName();
        }
        Map<String, String> pathParamMap = methodParam.getPathParamMap();
        if (pathParamMap.containsKey(pathParameter)) {
            return JsonUtils.str2Object(pathParamMap.get(pathParameter), parameter.getType());
        } else {
            throw new IllegalArgumentException("The specified parameter " + pathParameter + " can not be null!");
        }
    }
}
