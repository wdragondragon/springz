package org.jdragon.springz.feign.core;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONException;
import org.jdragon.springz.core.utils.AnnotationUtils;
import org.jdragon.springz.feign.annotation.ZFeign;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.http.HttpException;
import org.jdragon.springz.utils.http.HttpUtils;
import org.jdragon.springz.utils.json.FastJsonMemory;
import org.jdragon.springz.web.annotation.*;

/**
 * jdk8动态代理
 */
public class DynaProxyHttp implements InvocationHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final static Map<Class<?>, Object> proxyMap = new HashMap<>();

    private final static Map<Class<?>, Map<String, String>> globalParam = new HashMap<>();

    private final static List<String> noProxyMethod =
            Arrays.stream(Object.class.getDeclaredMethods()).map(Method::getName).collect(Collectors.toList());

    static {
        globalParam.put(RequestHeader.class, new HashMap<>());
        globalParam.put(RequestBody.class, new HashMap<>());
        globalParam.put(RequestParam.class, new HashMap<>());
    }

    private Class<?> object;

    private final String baseUrl;

    //深入解构
    private final String depth;

    private final String successField;

    private final String successCode;

    private final String messageField;

    private final boolean methodFallback;

    private final Object zFeignFallback;

    private final String[] headers;

    private DynaProxyHttp(Class<?> proxyInterface) {
        ZFeign zFeign = proxyInterface.getAnnotation(ZFeign.class);
        if (zFeign == null) {
            throw new RuntimeException("zFeign注解不存在，无法创建zFeign实例");
        }
        this.baseUrl = zFeign.baseUrl() + zFeign.basePath();
        this.depth = zFeign.depth();
        this.headers = zFeign.headers();
        this.successCode = zFeign.successCode();
        this.successField = zFeign.successField();
        this.messageField = zFeign.messageField();
        this.object = proxyInterface;
        try {
            if (Object.class == zFeign.fallback()) {
                this.methodFallback = false;
                this.zFeignFallback = null;
                return;
            } else if (proxyInterface.isAssignableFrom(zFeign.fallback())) {
                this.methodFallback = true;
            } else if (ZFeignFallback.class.isAssignableFrom(zFeign.fallback())) {
                this.methodFallback = false;
            } else {
                throw new RuntimeException("fall back类异常");
            }
            this.zFeignFallback = zFeign.fallback().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressWarnings("unchecked")
    public synchronized static <T> T getInstance(Class<T> proxyInterface) {
        T proxy = (T) proxyMap.get(proxyInterface);
        if (proxy == null) {
            DynaProxyHttp dynaProxyHttp = new DynaProxyHttp(proxyInterface);
            proxy = (T) Proxy.newProxyInstance(
                    proxyInterface.getClassLoader(),
                    new Class[]{proxyInterface},
                    dynaProxyHttp);
            proxyMap.put(proxyInterface, proxy);
        }
        return proxy;
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
        String url = "";
        try {
            StringBuilder urlBuilder = new StringBuilder(baseUrl);

            RequestMapping requestMapping = (RequestMapping) AnnotationUtils.getAllContainedAnnotationType(method, RequestMapping.class);

            if (requestMapping == null) {
                if (noProxyMethod.contains(method.getName())) {
                    return method.invoke(object, args);
                } else {
                    log.warn("该feign方法没有映射请求路径[{},{}]", object.getName(), method.getName());
                    return null;
                }
            }

            //解析头
            Map<String, String> headerMap = new HashMap<>();
            for (String header : headers) {
                String[] split = header.split("=");
                if (split.length != 2) {
                    log.warn("feign设置头部异常{}", header);
                    return null;
                }
                headerMap.put(split[0], split[1]);
            }

            urlBuilder.append((String) AnnotationUtils.getIncludeAnnotationValue(method, RequestMapping.class, "value"));

            RequestMethod[] request = requestMapping.method();

            HashMap<String, String> params = new HashMap<>();

            Parameter[] parameters = method.getParameters();
            Object body = new Object();

            url = urlBuilder.toString();

            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                String value = String.valueOf(args[i]);
                if (param.isAnnotationPresent(RequestParam.class) && args[i] != null) {
                    params.put(param.getAnnotation(RequestParam.class).value(), value);
                } else if (param.isAnnotationPresent(RequestBody.class)) {
                    body = args[i];
                } else if (param.isAnnotationPresent(PathVariable.class)) {
                    String pathVar = param.getAnnotation(PathVariable.class).value();
                    url = url.replaceAll("\\{" + pathVar + "}", value);
                } else if (param.isAnnotationPresent(RequestHeader.class) && args[i] != null) {
                    String header = param.getAnnotation(RequestHeader.class).value();
                    headerMap.put(header, value);
                }
            }
            return robotHandle(url, request[0], headerMap, body, params, method.getGenericReturnType());
        } catch (HttpException e) {
            log.error("远程服务调用异常[{}]", url);
            Object obj = null;
            if (methodFallback) {
                try {
                    obj = method.invoke(zFeignFallback, args);
                    if (obj != null) {
                        return obj;
                    }
                } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            } else {
                obj = ((ZFeignFallback) zFeignFallback).fallback(e);
            }
            if (obj != null) {
                return obj;
            }
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object robotHandle(String url, RequestMethod request, Map<String, String> headerMap, Object body, Map<String, String> params, Type type) {
        String str = "";
        try {
            HttpUtils httpUtils = HttpUtils.initJson()
                    .setMethod(request.name())
                    .setUrl(url)
                    .setHeaderMap(headerMap)
                    .setHeaderMap(globalParam.get(RequestHeader.class))
                    .setParamMap(params)
                    .setParamMap(globalParam.get(RequestParam.class))
                    .setBody(body)
                    .setBody(globalParam.get(RequestBody.class));
            Map<String, String> resultMap = httpUtils.exec();
            str = checkResult(resultMap);
            FastJsonMemory memory = FastJsonMemory.init(str);
            String code = memory.get(successField, String.class);
            if (!successCode.equals(code)) {
                throw new HttpException(memory.get(messageField, String.class));
            }
            return memory.get(depth, type);
        } catch (JSONException e) {
            Class<?> typeCls = (Class<?>) type;
            return typeCls.cast(str);
        } catch (HttpException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public static void addGlobalHeader(String name, String value) {
        addGlobal(RequestHeader.class, name, value);
    }

    public static void addGlobalParam(String name, String value) {
        addGlobal(RequestParam.class, name, value);
    }

    public static void addGlobalBody(String name, String value) {
        addGlobal(RequestBody.class, name, value);
    }

    private static void addGlobal(Class<?> clazz, String name, String value) {
        Map<String, String> param = globalParam.computeIfAbsent(clazz, k -> new HashMap<>());
        param.put(name, value);
    }
}
