package org.jdragon.springz.web.core.servlet;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.26 13:04
 * @Description:
 */
public class LoginServlet extends Servlet {

    @Override
    public void doGet(Request req, Response rep) {
        Map<String, String> attributes = req.getAttributes();

        rep.println(JSON.toJSONString(attributes));
    }

    @Override
    public void doPost(Request req, Response rep) {

    }
}
