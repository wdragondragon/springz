package org.jdragon.springz.test.config;

import org.jdragon.springz.aop.annotation.After;
import org.jdragon.springz.aop.annotation.Aop;
import org.jdragon.springz.aop.annotation.Before;
import org.jdragon.springz.aop.annotation.Pointcut;
import org.jdragon.springz.aop.annotation.Throw;
import org.jdragon.springz.aop.core.entity.JoinPoint;
import org.jdragon.springz.core.annotation.Component;


import java.util.Arrays;

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
    public void beforeMethod(JoinPoint joinPoint) {
        System.out.println("aspectTest3切面,方法执行:" + joinPoint.getTargetMethod().getName());
    }

    @After
    public void afterMethod(JoinPoint joinPoint) {
        System.out.println("aspectTest3切面,方法后执行得到结果:" + joinPoint.getInvokeResult());
    }

    @Throw
    public void throwable(Throwable throwable) {
        System.out.println("执行异常处理");
        System.out.println(throwable.getMessage());
        System.out.println(throwable.getCause().toString());
        System.out.println(Arrays.toString(throwable.getStackTrace()));
    }
}