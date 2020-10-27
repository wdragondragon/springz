package org.jdragon.springz.web.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.26 12:43
 * @Description:
 */
public class Server {

    private ServerSocket server;

    private boolean flag = false;

    public static void main(String[] args) {
        Server server = new Server(8888);
        server.start();
    }

    public Server(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.flag = true;
        this.link(server);
    }

    public void link(ServerSocket server) {
        while (flag) {
            try {
                Socket client = server.accept();
                new Thread(new Dispatcher(client)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
