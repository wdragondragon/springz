package org.jdragon.springz.test.config;

import org.jdragon.springz.aop.annotation.Aop;
import org.jdragon.springz.aop.annotation.Before;
import org.jdragon.springz.aop.annotation.Pointcut;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.entry.MethodInvocation;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 08:34
 * @Description:
 */
@Aop
@Component
public class AspectTest3 {
    @Pointcut(value = "org.jdragon.springz.test.dao.CarDao", method = "resource*")
    public void pointcut() {

    }

    @Before
    public void before(MethodInvocation methodInvocation) {
        System.out.println("aspect3");
    }
}