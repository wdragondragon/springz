package org.jdragon.springz.web.annotation;

import io.netty.handler.codec.http.HttpMethod;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.26 22:11
 * @Description:
 */
public enum RequestMethod {

    GET(HttpMethod.GET),
    HEAD(HttpMethod.HEAD),
    POST(HttpMethod.POST),
    PUT(HttpMethod.PUT),
    PATCH(HttpMethod.PATCH),
    DELETE(HttpMethod.DELETE),
    OPTIONS(HttpMethod.OPTIONS),
    TRACE(HttpMethod.TRACE);

    private final HttpMethod httpMethod;

    RequestMethod(HttpMethod httpMethod){
        this.httpMethod = httpMethod;
    }

    public static RequestMethod getRequestMethod(HttpMethod httpMethod){
        for (RequestMethod value : RequestMethod.values()) {
            if(value.httpMethod.equals(httpMethod)){
                return value;
            }
        }
        throw new IllegalArgumentException("没有这种http请求方法");
    }

}
