package org.jdragon.springz.web.core.entity;

import lombok.Data;
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
    @Value("${port}")
    private int port = 8080;
}
