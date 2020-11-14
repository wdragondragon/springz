package org.jdragon.springz.web.core.factory;

import io.netty.handler.codec.http.HttpMethod;
import org.jdragon.springz.web.core.handler.RequestHandler;
import org.jdragon.springz.web.core.handler.Handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.29 20:11
 * @Description:
 */
public class RequestHandlerFactory {

    public static final Map<HttpMethod, Handler> REQUEST_HANDLERS = new HashMap<>();

    static {
        REQUEST_HANDLERS.put(HttpMethod.GET, new RequestHandler());
        REQUEST_HANDLERS.put(HttpMethod.POST, new RequestHandler());
    }

    public static Handler get(HttpMethod httpMethod) {
        if (!REQUEST_HANDLERS.containsKey(httpMethod)) {
            throw new IllegalArgumentException("不支持该类型请求:" + httpMethod.name());
        }
        return REQUEST_HANDLERS.get(httpMethod);
    }
}