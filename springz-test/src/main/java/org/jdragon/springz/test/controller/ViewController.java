package org.jdragon.springz.test.controller;

import org.jdragon.springz.core.annotation.Controller;
import org.jdragon.springz.web.annotation.GetMapping;
import org.jdragon.springz.web.annotation.RequestMapping;
import org.jdragon.springz.web.core.entity.WebRequest;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.16 00:18
 * @Description:
 */
@Controller
@RequestMapping("/view")
public class ViewController {
    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/webRequest")
    public String test(WebRequest webRequest){
        webRequest.getAttributes().put("message","测试");
        return "webRequest";
    }
}
