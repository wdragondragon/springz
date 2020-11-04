package org.jdragon.springz.test.config;

import org.jdragon.springz.aop.annotation.After;
import org.jdragon.springz.aop.annotation.Aop;
import org.jdragon.springz.aop.annotation.Before;
import org.jdragon.springz.aop.annotation.Pointcut;
import org.jdragon.springz.aop.annotation.Throw;
import org.jdragon.springz.aop.core.entity.JoinPoint;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;


import java.util.Arrays;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 08:34
 * @Description:
 */
@Aop
@Component
public class AopCarDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut(value = "org.jdragon.springz.test.dao.CarDao", method = "resource*")
    public void pointcut() {

    }

    @Before
    public void beforeMethod(JoinPoint joinPoint) {
        logger.debug("CarDao切面,方法执行:" + joinPoint.getTargetMethod().getName());
    }

    @After
    public void afterMethod(JoinPoint joinPoint) {
        logger.debug("CarDao切面,方法后执行得到结果:" + joinPoint.getInvokeResult());
    }

    @Throw
    public void throwable(Throwable throwable) {
        logger.debug("执行异常处理");
    }
}