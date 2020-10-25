package org.jdragon.springz.test.service;

import org.jdragon.springz.feign.annotation.*;
import org.jdragon.springz.test.dao.Http;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.22 08:24
 * @Description:
 */
@ZFeign(baseUrl = "http://localhost:8081", basePath = "/test",depth = "result")
public interface HttpTest {
    //get http://localhost:8081/test/http
    @GetMapping("/http")
    public String http();

    //get http://localhost:8081/test/http1/{id}
    @GetMapping("/http1/{id}")
    public String http1(@PathVariable("id") int id);

    //get http://localhost:8081/test/http2?id={id}
    @GetMapping("/http2")
    public String http2(@RequestParam("id") int id);

    //带body的get http://localhost:8081/test/http3
    @GetMapping("/http3")
    public String http3(@RequestBody int id);

    //post http://localhost:8081/test/http4
    @PostMapping("/http4")
    public String http4();

    //post http://localhost:8081/test/http5/{id}
    @PostMapping("/http5/{id}")
    public String http5(@PathVariable("id") int id);

    //post http://localhost:8081/test/http6?id={id}
    @PostMapping("/http6")
    public String http6(@RequestParam("id") int id);

    //带body的post http://localhost:8081/test/http7
    @PostMapping("/http7")
    public String http7(@RequestBody Http test);

    @GetMapping("/http8")
    int http8();
}

