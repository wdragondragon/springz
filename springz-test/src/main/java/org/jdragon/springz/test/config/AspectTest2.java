package org.jdragon.springz.test.config;

import org.jdragon.springz.aop.annotation.*;
import org.jdragon.springz.aop.core.entity.JoinPoint;
import org.jdragon.springz.core.annotation.Component;

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
    @Pointcut("org.jdragon.springz.test.service.feign.HttpTest")
    public void pointcut() {

    }

    @Before
    public void before(JoinPoint joinPoint) {
        System.out.println("aspectTest2切面：执行方法：" + joinPoint.getTargetMethod().getName());
    }

    @After
    public void after(JoinPoint joinPoint) {
        System.out.println("aspectTest2切面：执行方法结果：" + joinPoint.getInvokeResult());

    }

    @Throw
    public void throwable(Throwable e){
        e.printStackTrace();
    }
}