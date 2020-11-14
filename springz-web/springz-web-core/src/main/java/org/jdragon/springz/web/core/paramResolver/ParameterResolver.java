package org.jdragon.springz.web.core.paramResolver;

import org.jdragon.springz.web.core.entity.MethodParam;

import java.lang.reflect.Parameter;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.14 14:25
 * @Description:
 */
public interface ParameterResolver {
    /**
     * Process method parameters
     *
     * @param methodParam Target method related information
     * @param parameter    The parameter of the target method
     * @return Specific values ​​corresponding to the parameters of the target method
     */
    Object resolve(MethodParam methodParam, Parameter parameter);
}
