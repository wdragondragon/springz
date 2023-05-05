package org.jdragon.springz.web.core.resolver;

import io.netty.handler.codec.http.HttpHeaders;
import org.jdragon.springz.utils.json.JsonUtils;
import org.jdragon.springz.web.annotation.RequestHeader;
import org.jdragon.springz.web.core.entity.MethodParam;

import java.lang.reflect.Parameter;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 21:36
 * @Description:
 */
public class RequestHeaderResolver implements ParameterResolver {
    @Override
    public Object resolve(MethodParam methodParam, Parameter parameter) {
        RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);
        String value = requestHeader.value();
        if (value.isEmpty()) {
            value = parameter.getName();
        }
        HttpHeaders header = methodParam.getHeader();
        if (header.contains(value)) {
            return JsonUtils.str2Object(header.get(value), parameter.getType());
        } else {
            return null;
        }
    }
}
