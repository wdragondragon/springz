package org.jdragon.springz.test.config;

import org.jdragon.springz.aop.annotation.Aop;
import org.jdragon.springz.aop.annotation.Before;
import org.jdragon.springz.aop.annotation.Order;
import org.jdragon.springz.aop.annotation.Pointcut;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.entry.MethodInvocation;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 02:14
 * @Description:
 */
@Aop
@Order(1)
@Component
public class AspectTest2 {
    @Pointcut("org.jdragon.springz.test.service.HttpTest")
    public void pointcut(){

    }

    @Before
    public void before(MethodInvocation methodInvocation){
        System.out.println("aspect2");
    }
}