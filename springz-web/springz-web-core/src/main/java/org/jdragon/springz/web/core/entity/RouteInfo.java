package org.jdragon.springz.web.core.entity;

import lombok.Builder;
import lombok.Data;
import org.jdragon.springz.scanner.entry.BeanInfo;
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
@Builder
public class RouteInfo {

    private RequestMethod[] requestMethod;

    private String requestUrl;

    private String bindBeanName;

    private Object bindObj;

    private Method bindMethod;

    private Parameter[] invokeParams;
}
