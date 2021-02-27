package org.jdragon.springz.web.core.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.16 22:12
 * @Description:
 */
@Data
public class WebRequest {

    private final MethodParam methodParam;

    private final Map<String, Object> attributes = new HashMap<>();

    public WebRequest(MethodParam methodParam) {
        this.methodParam = methodParam;
    }
}
