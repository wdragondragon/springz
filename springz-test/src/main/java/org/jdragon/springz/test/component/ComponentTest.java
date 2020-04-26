package org.jdragon.springz.test.component;


import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.test.domain.User;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 15:05
 * @Description:
 */
@Component
public class ComponentTest {
    @Autowired
    private static User user;

    @Autowired
    private static User user2;

    public void test() {
        System.out.println("ComponentTest~~");
    }

    public void scopeTest(){
        user.setUsername("李四");
        user2.setUsername("黄五");
        System.out.println(user);
        System.out.println(user2);
    }
}
