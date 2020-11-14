package org.jdragon.springz.web.core.netty;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.11 08:22
 * @Description:
 */
public class NettyServer {
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();
        httpServer.start();
    }
}
