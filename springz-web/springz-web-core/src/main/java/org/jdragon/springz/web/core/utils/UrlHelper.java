package org.jdragon.springz.web.core.utils;

import org.jdragon.springz.utils.StrUtil;
import org.jdragon.springz.web.annotation.RequestMethod;
import org.jdragon.springz.web.core.RouteMethodMapper;
import org.jdragon.springz.web.core.entity.RouteInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 17:31
 * @Description:
 */
public class UrlHelper {

    /**
     * 根据请求路径和请求方法来确定是否有匹配的路由，有则返回路由路径
     * @param requestMethod 请求方法
     * @param path 请求路径
     * @return
     */
    public static String getMatchingPath(RequestMethod requestMethod, String path) {
        Map<String, RouteInfo> stringRouteInfoMap = RouteMethodMapper.getRouteMapping(requestMethod);
        if (stringRouteInfoMap.containsKey(path)) {
            return path;
        }

        String[] pathSplit = path.split("/");
        for (String r : stringRouteInfoMap.keySet()) {
            String[] keySplit = r.split("/");
            //不是相同长度的话，不匹配
            if (keySplit.length != pathSplit.length) {
                continue;
            }
            //根据/切割，逐一匹配
            boolean paramMatch = true;
            for (int i = 0; i < keySplit.length; i++) {
                String value = pathSplit[i];
                String key = keySplit[i];
                //如果不相同则进入{}的参数匹配
                if (!value.equals(key)) {
                    key = StrUtil.matchWrap(key, "\\{", "}");
                    if (key.isEmpty()) {
                        //如果出现即不相同，又不为{}的情况，则不匹配
                        paramMatch = false;
                        break;
                    }
                }
            }
            //如果匹配的话routeInfo赋值
            if (paramMatch) {
                return r;
            }
        }
        return null;
    }

    /**
     * 根据请求路径和路由路径来获取path参数map
     * @param requestPath 实际的请求路径
     * @param routePath 通过路由获取的路由路径
     * @return path参数
     */
    public static Map<String, String> analyzePathParams(String requestPath, String routePath) {
        Map<String, String> pathParams = new HashMap<>();
        String[] requestPathSplit = requestPath.split("/");
        String[] routePathSplit = routePath.split("/");
        if (requestPathSplit.length == routePathSplit.length) {
            for (int i = 0; i < requestPathSplit.length; i++) {
                if (!routePathSplit[i].equals(requestPathSplit[i])) {
                    String key = StrUtil.matchWrap(routePathSplit[i], "\\{", "}");
                    pathParams.put(key, requestPathSplit[i]);
                }
            }
        }
        return pathParams;
    }
}
