package org.jdragon.springz.web.core.entity;

import org.jdragon.springz.web.annotation.RequestMethod;

import java.lang.reflect.Method;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.27 21:15
 * @Description:
 */
public class RouteInfo {

    private RequestMethod requestMethod;

    private String requestUrl;

    private Class<?> bindObj;

    private Method bindMethod;

}
