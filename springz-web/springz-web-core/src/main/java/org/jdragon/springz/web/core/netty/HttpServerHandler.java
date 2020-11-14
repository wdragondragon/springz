package org.jdragon.springz.web.core.netty;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.jdragon.springz.utils.MethodUtils;
import org.jdragon.springz.utils.StrUtil;
import org.jdragon.springz.utils.http.response.Result;
import org.jdragon.springz.web.annotation.RequestMethod;
import org.jdragon.springz.web.core.RouteMethodMapperContainer;
import org.jdragon.springz.web.core.entity.MethodParam;
import org.jdragon.springz.web.core.entity.RouteInfo;
import org.jdragon.springz.web.core.factory.ParameterResolverFactory;
import org.jdragon.springz.web.core.paramResolver.ParameterResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.11 08:23
 * @Description:
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final String FAVICON_ICO = "/favicon.ico";
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        log.info("Handle http request:{}", fullHttpRequest);
        String uri = fullHttpRequest.uri();
        QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
        String path = queryDecoder.path();
        HttpMethod method = fullHttpRequest.method();
        Map<String, List<String>> urlParamsMap = queryDecoder.parameters();
        Map<String, String> pathParamMap = new HashMap<>();
        String body = fullHttpRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));

        if (FAVICON_ICO.equals(path)) return;

        RequestMethod requestMethod = RequestMethod.getRequestMethod(method);

        Map<String, RouteInfo> stringRouteInfoMap = RouteMethodMapperContainer.getRouteMapping(requestMethod);

        boolean lastMatch = stringRouteInfoMap.containsKey(path);

        if (!lastMatch) {
            String[] pathSplit = path.split("/");
            for (String r : stringRouteInfoMap.keySet()) {
                pathParamMap.clear();
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
                        if (!key.isEmpty()) {
                            pathParamMap.put(key, value);
                        } else {
                            //如果出现即不相同，又不为{}的情况，则不匹配
                            paramMatch = false;
                            break;
                        }
                    }
                }
                //如果匹配的话routeInfo赋值
                if (paramMatch) {
                    lastMatch = true;
                    path = r;
                    break;
                }
            }
        }
        byte[] bytes;
        //最后routeInfo还是空则确认不匹配
        if (lastMatch) {
            RouteInfo routeInfo = stringRouteInfoMap.get(path);
            Parameter[] parameters = routeInfo.getInvokeParams();
            List<Object> values = new ArrayList<>();
            for (Parameter parameter : parameters) {
                ParameterResolver parameterResolver = ParameterResolverFactory.get(parameter);
                if (parameterResolver == null) {
                    throw new IllegalArgumentException("参数缺少web注解");
                }
                MethodParam methodParam = new MethodParam(urlParamsMap, pathParamMap, body);
                Object resolve = parameterResolver.resolve(methodParam, parameter);
                values.add(resolve);
            }
            Method bindMethod = routeInfo.getBindMethod();
            Object invoke = MethodUtils.invoke(routeInfo.getBindObj(), bindMethod, values.toArray());
            if(bindMethod.getGenericReturnType().equals(void.class)){
                bytes = new byte[0];
            }else{
                bytes = JSON.toJSONBytes(invoke);
            }

        } else {
            bytes = JSON.toJSONBytes(Result.error());
        }
        System.out.println(queryDecoder.toString());
        System.out.println("header:::" + fullHttpRequest.headers().entries());
        System.out.println("uri:::" + uri);
        System.out.println("param:::" + urlParamsMap);
        System.out.println("path:::" + pathParamMap);
        System.out.println("body:::" + body);

//        if (uri.equals(FAVICON_ICO)) {
//            return;
//        }
//        RequestHandler requestHandler = RequestHandlerFactory.get(fullHttpRequest.method());
//        FullHttpResponse fullHttpResponse;
//        try {
//            fullHttpResponse = requestHandler.handle(fullHttpRequest);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            String requestPath = UrlUtil.getRequestPath(fullHttpRequest.uri());
//            fullHttpResponse = FullHttpResponseFactory.getErrorResponse(requestPath, e.toString(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
//        }
//        boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
//        if (!keepAlive) {
//            ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
//        } else {
//            fullHttpResponse.headers().set(CONNECTION, KEEP_ALIVE);
//            ctx.write(fullHttpResponse);
//        }
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytes));

        response.headers().set(AsciiString.cached("Content-Type"), "application/json");
        response.headers().setInt(AsciiString.cached("Content-Length"), response.content().readableBytes());
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


}
