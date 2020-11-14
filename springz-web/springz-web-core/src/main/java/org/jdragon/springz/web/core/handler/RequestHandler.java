package org.jdragon.springz.web.core.handler;

import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.*;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.jdragon.springz.utils.http.response.Result;
import org.jdragon.springz.utils.json.JsonUtils;
import org.jdragon.springz.web.annotation.RequestMethod;
import org.jdragon.springz.web.core.RouteMethodMapper;
import org.jdragon.springz.web.core.entity.MethodParam;
import org.jdragon.springz.web.core.entity.RouteInfo;
import org.jdragon.springz.web.core.factory.FullHttpResponseFactory;
import org.jdragon.springz.web.core.utils.UrlHelper;

import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.29 20:22
 * @Description:
 */
public class RequestHandler implements Handler {
    @Override
    public FullHttpResponse handle(FullHttpRequest fullHttpRequest) {
        String uri = fullHttpRequest.uri();
        QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
        String path = queryDecoder.path();

        HttpMethod method = fullHttpRequest.method();
        RequestMethod requestMethod = RequestMethod.getRequestMethod(method);

        String matchingPath = UrlHelper.getMatchingPath(requestMethod, path);

        byte[] result;
        //空则确认不匹配
        if (matchingPath != null) {
            //封装请求参数
            String body = fullHttpRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            MethodParam methodParam = new MethodParam(
                    fullHttpRequest.headers(),
                    queryDecoder.parameters(),
                    UrlHelper.analyzePathParams(path, matchingPath),
                    body);

            RouteInfo routeInfo = RouteMethodMapper.getRoute(requestMethod, matchingPath);
            result = routeInfo.invokeMethod(methodParam);
        } else {
            Result<Object> error = Result.error();
            error.setCode(404L);
            error.setMessage("路径未找到");
            result = JsonUtils.object2Byte(error);
        }

        return FullHttpResponseFactory.buildSuccessResponse(result);
    }
}
