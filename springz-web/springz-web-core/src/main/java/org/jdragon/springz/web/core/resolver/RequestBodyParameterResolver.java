package org.jdragon.springz.web.core.resolver;

import org.jdragon.springz.utils.json.JsonUtils;
import org.jdragon.springz.web.core.entity.MethodParam;

import java.lang.reflect.Parameter;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 14:26
 * @Description:
 */
public class RequestBodyParameterResolver implements ParameterResolver {
    @Override
    public Object resolve(MethodParam methodParam, Parameter parameter) {
        String body = methodParam.getBody();
        if (body == null || body.isEmpty()) {
            return null;
        } else {
            return JsonUtils.json2Object(methodParam.getBody(), parameter.getType());
        }
    }
}
