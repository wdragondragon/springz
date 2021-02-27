package org.jdragon.springz.web.core.entity;

import lombok.Data;
import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.Properties;
import org.jdragon.springz.core.annotation.Value;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 22:56
 * @Description:
 */

@Data
@Component
@Properties(prefix = "server.http")
public class HttpProperty {

    @AutowiredZ
    private static HttpProperty Instance;

    @Value("${port}")
    private int port = 8080;

    @Value("${prefix}")
    private String prefix = "/template/";

    @Value("${suffix}")
    private String suffix = "html";

    public static HttpProperty getInstance() {
        return Instance;
    }
}