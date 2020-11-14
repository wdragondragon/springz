package org.jdragon.springz.web.core.paramResolver;

import com.alibaba.fastjson.JSON;
import org.jdragon.springz.web.core.entity.MethodParam;

import java.lang.reflect.Parameter;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 14:26
 * @Description:
 */
public class RequestBodyParameterResolver implements ParameterResolver{
    @Override
    public Object resolve(MethodParam methodParam, Parameter parameter) {
        return JSON.parseObject(methodParam.getBody(),parameter.getType());
    }
}
