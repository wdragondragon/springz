package org.jdragon.springz.test.service;

import com.jdragon.common.response.normal.Result;
import org.jdragon.springz.annotation.feign.*;
import org.jdragon.springz.test.dao.Http;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.22 08:24
 * @Description:
 */
@ZFeign(baseUrl = "http://localhost:8081", basePath = "/test",depth = "result")
public interface HttpTest {

    @GetMapping("/http")
    public String http();

    @GetMapping("/http1/{id}")
    public String http1(@PathVariable("id") int id);

    @GetMapping("/http2")
    public String http2(@RequestParam("id") int id);

    @GetMapping("/http3")
    public String http3(@RequestBody int id);

    @PostMapping("/http4")
    public String http4();

    @PostMapping("/http5/{id}")
    public String http5(@PathVariable("id") int id);

    @PostMapping("/http6")
    public String http6(@RequestParam("id") int id);

    @PostMapping("/http7")
    public String http7(@RequestBody Http test);
}

