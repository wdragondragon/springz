package org.jdragon.springz.web.core.factory;

import org.jdragon.springz.web.annotation.RequestMethod;
import org.jdragon.springz.web.core.handler.GetRequestHandler;
import org.jdragon.springz.web.core.handler.PostRequestHandler;
import org.jdragon.springz.web.core.handler.RequestHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.29 20:11
 * @Description:
 */
public class RequestHandlerFactory {

    public static final Map<RequestMethod, RequestHandler> REQUEST_HANDLERS = new HashMap<>();

    static {
        REQUEST_HANDLERS.put(RequestMethod.GET, new GetRequestHandler());
        REQUEST_HANDLERS.put(RequestMethod.POST, new PostRequestHandler());
    }

    public static RequestHandler get(RequestMethod httpMethod) {
        return REQUEST_HANDLERS.get(httpMethod);
    }
}