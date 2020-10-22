package org.jdragon.springz.test.component;



import org.jdragon.springz.annotation.core.AutowiredZ;
import org.jdragon.springz.annotation.core.Component;
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

    @AutowiredZ
    private static User user;

    @AutowiredZ
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
