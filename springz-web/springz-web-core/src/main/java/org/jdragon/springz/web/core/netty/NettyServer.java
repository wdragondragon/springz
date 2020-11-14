package org.jdragon.springz.web.core.netty;

import org.jdragon.springz.core.annotation.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.11 08:22
 * @Description:
 */
//@Component
public class NettyServer {

//    @PostConstruct
//    public void start(){
//        HttpServer httpServer = new HttpServer();
//        httpServer.start();
//    }
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();
        httpServer.start();
    }
}
