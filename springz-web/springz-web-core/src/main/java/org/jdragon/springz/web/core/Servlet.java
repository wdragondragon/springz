package org.jdragon.springz.web.core;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.26 12:53
 * @Description:
 */
public abstract class Servlet {


    public Servlet() {

    }

    public void service(Request req,Response rep) {
        if(req.getMethod().equalsIgnoreCase("get")) {
            this.doGet(req, rep);
        }else {
            this.doPost(req, rep);
        }
    }

    public abstract void doGet(Request req,Response rep);

    public abstract void doPost(Request req,Response rep);
}