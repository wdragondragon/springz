package org.jdragon.springz.web.core;

import java.io.IOException;
import java.net.Socket;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.26 12:44
 * @Description:
 */
public class Dispatcher implements Runnable {
    private Socket client;
    private Request req;
    private Response rep;
    private int code = 200;

    public Dispatcher(Socket client) {
        this.client = client;
        try {
            req = new Request(client.getInputStream());
            rep = new Response(client.getOutputStream());
        } catch (IOException e) {
            code = 500;
        }
    }

    @Override
    public void run() {
        System.out.println(req.getUrl() + "   ***");
        Servlet servlet = Webapp.getServlet(req.getUrl());
        if (servlet != null)
            servlet.service(req, rep);
        else
            code = 404;
        try {
            rep.pushToClient(code);
        } catch (IOException e) {
            code = 500;
        }
        try {
            rep.pushToClient(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CloseUtil.closeAll(client);
    }
}
