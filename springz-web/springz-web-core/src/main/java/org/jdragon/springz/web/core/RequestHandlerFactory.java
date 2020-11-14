package org.jdragon.springz.web.core;

import io.netty.handler.codec.http.HttpMethod;
import org.jdragon.springz.web.core.handler.GetRequestHandler;
import org.jdragon.springz.web.core.handler.PostRequestHandler;
import org.jdragon.springz.web.core.handler.RequestHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.11 08:51
 * @Description:
 */
public class RequestHandlerFactory {
    public static final Map<HttpMethod, RequestHandler> REQUEST_HANDLERS = new HashMap<>();

    static {
        REQUEST_HANDLERS.put(HttpMethod.GET, new GetRequestHandler());
        REQUEST_HANDLERS.put(HttpMethod.POST, new PostRequestHandler());
    }

    public static RequestHandler get(HttpMethod httpMethod) {
        return REQUEST_HANDLERS.get(httpMethod);
    }
}
