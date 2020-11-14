package org.jdragon.springz.web.core.resolver;

import com.alibaba.fastjson.JSON;
import org.jdragon.springz.web.annotation.PathVariable;
import org.jdragon.springz.web.core.entity.MethodParam;

import java.lang.reflect.Parameter;

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
        String pathParam = methodParam.getPathParamMap().get(pathVariable.value());
        return JSON.parseObject(pathParam, parameter.getType());
    }
}
