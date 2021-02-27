package org.jdragon.springz.web.core.entity;

import lombok.Builder;
import lombok.Data;

import org.jdragon.springz.web.annotation.RequestMethod;


import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.27 21:15
 * @Description:
 */
@Data
public class RouteInfo {

    private RequestMethod[] requestMethod;

    private String requestUrl;

    private String bindBeanName;

    private Object bindObj;

    private Method bindMethod;

    private Parameter[] invokeParams;

    //使用使用数据模式，这取决于方法是否经过视图解析器返回视图
    private boolean useDataMode;

    @Builder
    public RouteInfo(RequestMethod[] requestMethod, String requestUrl, String bindBeanName,
                     Object bindObj, Method bindMethod, Parameter[] invokeParams, boolean useDataMode) {
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
        this.bindBeanName = bindBeanName;
        this.bindObj = bindObj;
        this.bindMethod = bindMethod;
        this.invokeParams = invokeParams;
        this.useDataMode = useDataMode;
    }
}
