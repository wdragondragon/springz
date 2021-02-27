package org.jdragon.springz.web.core.factory;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AsciiString;
import org.jdragon.springz.web.core.entity.ResponseData;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 17:58
 * @Description:
 */
public class FullHttpResponseFactory {

    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");

    public static FullHttpResponse buildSuccessResponse(ResponseData responseData) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(responseData.getData()));
        response.headers().set(CONTENT_TYPE, responseData.getContentType());
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }
}
