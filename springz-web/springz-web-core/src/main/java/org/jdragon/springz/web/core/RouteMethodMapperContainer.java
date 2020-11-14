package org.jdragon.springz.web.core;

import io.netty.handler.codec.http.HttpMethod;
import lombok.Getter;
import org.jdragon.springz.web.annotation.RequestMethod;
import org.jdragon.springz.web.core.entity.RouteInfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.11 10:42
 * @Description: 用作存储请求路径与方法映射
 */
public class RouteMethodMapperContainer {

    @Getter
    private static final Map<RequestMethod, Map<String, RouteInfo>> routeMapping = new HashMap<>();

    public static void registrar(RouteInfo routeInfo) {
        RequestMethod[] requestMethods = routeInfo.getRequestMethod();
        String requestUrl = routeInfo.getRequestUrl();

        for (RequestMethod requestMethod : requestMethods) {
            if (!routeMapping.containsKey(requestMethod)) {
                routeMapping.put(requestMethod, new LinkedHashMap<>());
            }
            routeMapping.get(requestMethod)
                    .put(requestUrl, routeInfo);
        }
    }


    public static Map<String,RouteInfo> getRouteMapping(RequestMethod requestMethod){
        return routeMapping.get(requestMethod);
    }

}
