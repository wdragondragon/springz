package org.jdragon.springz.test.component;




import org.jdragon.springz.core.annotation.Inject;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import javax.annotation.PostConstruct;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 15:05
 * @Description:
 */
@Component
public class ComponentTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private static User user;

    @Inject
    private static User user2;

    public void test() {
        logger.trace("ComponentTest~~");
    }

    public void scopeTest(){
        user.setUsername("李四");
        user2.setUsername("黄五");
        System.out.println(user);
        System.out.println(user2);
        logger.trace(user.toString());
        logger.trace(user2.toString());
    }

    @PostConstruct
    public void init(){
        logger.warn("PostConstruct");
    }
}
