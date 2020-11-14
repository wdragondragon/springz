package org.jdragon.springz.web.core.entity;

import io.netty.handler.codec.http.HttpHeaders;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 14:20
 * @Description: 将pathParam,urlParam,body封装
 */
@AllArgsConstructor
@Data
public class MethodParam {

    private HttpHeaders header;

    private Map<String, List<String>> urlParamsMap;

    private Map<String, String> pathParamMap;

    private String body;


}
