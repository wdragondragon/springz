package org.jdragon.springz.web.core.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.29 20:10
 * @Description:
 */
public interface Handler {
    FullHttpResponse handle(FullHttpRequest fullHttpRequest);
}
