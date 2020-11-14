package org.jdragon.springz.web.core.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Builder;
import lombok.Data;
import org.jdragon.springz.utils.MethodUtils;
import org.jdragon.springz.utils.json.JsonUtils;
import org.jdragon.springz.web.annotation.RequestMethod;
import org.jdragon.springz.web.core.factory.ParameterResolverFactory;
import org.jdragon.springz.web.core.resolver.ParameterResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.27 21:15
 * @Description:
 */
@Data
@Builder
public class RouteInfo {

    private RequestMethod[] requestMethod;

    private String requestUrl;

    private String bindBeanName;

    private Object bindObj;

    private Method bindMethod;

    private Parameter[] invokeParams;

    public byte[] invokeMethod(MethodParam methodParam) {
        List<Object> values = new ArrayList<>();
        for (Parameter parameter : invokeParams) {
            ParameterResolver parameterResolver = ParameterResolverFactory.get(parameter);
            if (parameterResolver == null) {
                throw new IllegalArgumentException("参数缺少web注解");
            }
            Object resolve = parameterResolver.resolve(methodParam, parameter);
            values.add(resolve);
        }
        Object result = MethodUtils.invoke(bindObj, bindMethod, values.toArray());
        if (bindMethod.getGenericReturnType().equals(void.class)) {
            return new byte[0];
        } else {
            return JsonUtils.object2ByteIncludeNull(result);
        }
    }
}
