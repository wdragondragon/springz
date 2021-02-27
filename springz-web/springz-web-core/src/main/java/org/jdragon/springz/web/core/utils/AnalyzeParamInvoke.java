package org.jdragon.springz.web.core.utils;

import lombok.Getter;
import org.jdragon.springz.utils.MethodUtils;
import org.jdragon.springz.utils.json.JsonUtils;
import org.jdragon.springz.web.core.entity.*;
import org.jdragon.springz.web.core.factory.ParameterResolverFactory;
import org.jdragon.springz.web.core.netty.ContentType;
import org.jdragon.springz.web.core.resolver.ParameterResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.16 22:05
 * @Description:
 */
public class AnalyzeParamInvoke {

    public static AnalyzeParamInvoke get() {
        return new AnalyzeParamInvoke();
    }

    private final List<Class<?>> injectionTypes = new ArrayList<>();

    {
        injectionTypes.add(WebRequest.class);
    }

    /**
     * 执行路由映射的方法
     * @param routeInfo 路由信息
     * @param methodParam 方法参数封装，包括header param body path参数
     * @return 返回数据
     */
    public ResponseData invokeMethod(RouteInfo routeInfo, MethodParam methodParam) {
        Parameter[] invokeParams = routeInfo.getInvokeParams();
        Object bindObj = routeInfo.getBindObj();
        Method bindMethod = routeInfo.getBindMethod();
        boolean useDataMode = routeInfo.isUseDataMode();
        WebRequest webRequest = new WebRequest(methodParam);

        List<Object> values = new ArrayList<>();
        for (Parameter parameter : invokeParams) {
            ParameterResolver parameterResolver = ParameterResolverFactory.get(parameter);
            if (parameterResolver == null) {
                if (injectionTypes.contains(parameter.getType())) {
                    values.add(webRequest);
                } else {
                    values.add(null);
                }
            } else {
                Object resolve = parameterResolver.resolve(methodParam, parameter);
                values.add(resolve);
            }
        }
        Object result = MethodUtils.invoke(bindObj, bindMethod, values.toArray());
        if (useDataMode) {
            if (bindMethod.getGenericReturnType().equals(void.class)) {
                return new ResponseData(new byte[0], ContentType.TEXT_HTML);
            } else {
                return new ResponseData(JsonUtils.object2ByteIncludeNull(result), ContentType.APPLICATION_JSON);
            }
        } else if (result instanceof String) {
            HttpProperty httpProperty = HttpProperty.getInstance();
            FreeMakerUtil freeMakerUtil = new FreeMakerUtil(httpProperty.getPrefix(), httpProperty.getSuffix());
            String s = freeMakerUtil.printString((String) result, webRequest.getAttributes());
            return new ResponseData(s.getBytes(), ContentType.TEXT_HTML);
        } else {
            return new ResponseData(new byte[0], ContentType.TEXT_HTML);
        }
    }
}
