package org.jdragon.springz.test.config;

import org.jdragon.springz.aop.annotation.*;
import org.jdragon.springz.aop.core.entity.JoinPoint;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.04 17:37
 * @Description:
 */
@Aop(order = 2)
@Component
public class AopHttpTest2 {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("org.jdragon.springz.test.service.feign.HttpTest")
    public void pointcut() {

    }

    @Before
    public void before(JoinPoint joinPoint) {
        logger.debug("HttpTest切面B：执行方法：" + joinPoint.getTargetMethod().getName());
    }

    @After
    public void after(JoinPoint joinPoint) {
        logger.debug("HttpTest切面B：执行方法结果：" + joinPoint.getInvokeResult());

    }
}
