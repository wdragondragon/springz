package org.jdragon.springz.web.core.paramResolver;

import com.alibaba.fastjson.JSON;
import org.jdragon.springz.web.annotation.RequestParam;
import org.jdragon.springz.web.core.entity.MethodParam;

import java.lang.reflect.Parameter;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 14:26
 * @Description:
 */
public class RequestParamParameterResolver implements ParameterResolver {
    @Override
    public Object resolve(MethodParam methodParam, Parameter parameter) {
        Class<?> type = parameter.getType();
        RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
        String requestParameter = requestParam.value();
        List<String> list = methodParam.getUrlParamsMap().get(requestParameter);
        if (list == null) {
            throw new IllegalArgumentException("The specified parameter " + requestParameter + " can not be null!");
        }
        String s;
        if (type.isArray()) {
            s = JSON.toJSONString(list);
        } else {
            s = list.get(0);
        }
        return JSON.parseObject(s, type);
    }
}
