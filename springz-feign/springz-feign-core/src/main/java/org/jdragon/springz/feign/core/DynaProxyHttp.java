package org.jdragon.springz.feign.core;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

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

    private Class<?> object;

    private final String baseUrl;

    //深入解构
    private final String depth;

    private final String[] headers;

    public DynaProxyHttp(ZFeign zFeign) {
        this.baseUrl = zFeign.baseUrl() + zFeign.basePath();
        this.depth = zFeign.depth();
        this.headers = zFeign.headers();
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
                log.warn("该feign方法没有映射请求路径", object.getName(), method.getName());
                return null;
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
                if (param.isAnnotationPresent(RequestParam.class)) {
                    if (args[i] == null) {
                        continue;
                    }
                    params.put(param.getAnnotation(RequestParam.class).value(), value);
                } else if (param.isAnnotationPresent(RequestBody.class)) {
                    body = args[i];
                } else if (param.isAnnotationPresent(PathVariable.class)) {
                    String pathVar = param.getAnnotation(PathVariable.class).value();
                    url = url.replaceAll("\\{" + pathVar + "}", value);
                } else if(param.isAnnotationPresent(RequestHeader.class)){
                    String header = param.getAnnotation(RequestHeader.class).value();
                    headerMap.put(header, value);
                }
            }
            return robotHandle(url, request[0], headerMap, body, params, method.getGenericReturnType());
        } catch (HttpException e) {
            log.error("远程服务调用异常", url);
            e.printStackTrace();
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
                    .setParamMap(params)
                    .setBody(body);
            Map<String, String> resultMap = httpUtils.exec();
            str = checkResult(resultMap);
            FastJsonMemory memory = FastJsonMemory.init(str);
            return memory.get(depth, type);
        } catch (JSONException e) {
            Class<?> typeCls = (Class<?>) type;
            return typeCls.cast(str);
        } catch (Exception e) {
            log.error("远程服务调用异常", url);
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
}
