package org.jdragon.springz.test.component;


import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.test.App;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 15:05
 * @Description:
 */
@Component
public class ComponentTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private static User user;

    @Autowired
    private static User user2;

    public void test() {
        logger.trace("ComponentTest~~");
    }

    public void scopeTest(){
        user.setUsername("李四");
        user2.setUsername("黄五");
        logger.trace(user.toString());
        logger.trace(user2.toString());
    }
}
