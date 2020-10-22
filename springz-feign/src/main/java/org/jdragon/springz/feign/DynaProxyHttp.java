package org.jdragon.springz.feign;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jdragon.common.http.HttpException;
import com.jdragon.common.http.HttpUtils;
import com.jdragon.common.json.JsonUtils;
import org.jdragon.springz.annotation.feign.*;

/**
 * jdk8动态代理
 */
public class DynaProxyHttp implements InvocationHandler {
    private Class<?> object;

    private String baseUrl;

    //深入解构
    private String[] depth;

    public DynaProxyHttp(String baseUrl,String[] depth) {
        this.baseUrl = baseUrl;
        this.depth = depth;
    }

    @SuppressWarnings("unchecked")
    public <T> T bindInterface(Class<T> proxyInterface) {
        object = proxyInterface;
        return (T) Proxy.newProxyInstance(
                proxyInterface.getClassLoader(),
                new Class[]{proxyInterface},
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
//            result = method.invoke(this.object,args);
            StringBuilder urlBuilder = new StringBuilder(baseUrl);
            int request = 1;
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                urlBuilder.append(getMapping.value());
                request = 1;
            } else if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                urlBuilder.append(postMapping.value());
                request = 2;
            }


            HashMap<String, String> params = new HashMap<>();

            Parameter[] parameters = method.getParameters();
            Object body = new Object();

            String url = urlBuilder.toString();

            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                String value = String.valueOf(args[i]);
                if (param.isAnnotationPresent(RequestParam.class)) {
                    params.put(param.getAnnotation(RequestParam.class).value(), value);
                } else if (param.isAnnotationPresent(RequestBody.class)) {
                    body = args[i];
                } else if (param.isAnnotationPresent(PathVariable.class)) {
                    String pathVar = param.getAnnotation(PathVariable.class).value();
                    url = url.replaceAll("\\{" + pathVar + "}", value);
                }
            }
            return robotHandle(url, request, body, params, method.getGenericReturnType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object robotHandle(String url, int request, Object robotMsgResult, HashMap<String, String> params, Type type) {
        String str = "";
        try {
            HttpUtils httpUtils = HttpUtils.initJson();
            Map<String, String> map;

            if (robotMsgResult == null) return null;

            if (request == 1) {
                httpUtils.setParamMap(params);
                map = httpUtils.get(url);
            } else {
                String s = JsonUtils.object2Str(robotMsgResult);
                map = JSON.parseObject(s, new TypeReference<Map<String, String>>() {
                });
                httpUtils.setParamMap(params);
                httpUtils.setBody(map);
                map = httpUtils.post(url);
            }
            str = checkResult(map);
            JSONObject resultJson = JSON.parseObject(str);
            for (String s : depth) {
                str = resultJson.getString(s);
            }
            return JSON.parseObject(str,type);
        } catch (Exception e) {
            Class<?> typeCls = (Class<?>) type;
            return typeCls.cast(str);
        }
    }

    public static String checkResult(Map<String, String> result) {
        String statusCode = result.get("statusCode");
        String resultStr = result.get("result");
        if ("200".equals(statusCode)) {
            return resultStr;
        } else {
            throw new HttpException(resultStr);
        }
    }
}
