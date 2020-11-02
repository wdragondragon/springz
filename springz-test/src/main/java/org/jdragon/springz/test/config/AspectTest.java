package org.jdragon.springz.test.config;

import org.jdragon.springz.aop.annotation.*;
import org.jdragon.springz.aop.core.entity.JoinPoint;
import org.jdragon.springz.core.annotation.Component;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 01:39
 * @Description:
 */

@Aop
@Order(2)
@Component
public class AspectTest {

    @Pointcut("org.jdragon.springz.test.service.UserAddService*")
    public void pointcut() {

    }

    @Before
    public void beforeMethod(JoinPoint joinPoint) {
        System.out.println("aspectTest切面,方法执行:" + joinPoint.getTargetMethod().getName());
    }

    @After
    public void afterMethod(JoinPoint joinPoint) {
        System.out.println("aspectTest切面,方法后执行得到结果:" + joinPoint.getInvokeResult());
    }
}
