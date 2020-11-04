package org.jdragon.springz.test;

import org.jdragon.springz.ImportTest;
import org.jdragon.springz.core.SpringzContext;
import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.test.App;
import org.jdragon.springz.test.component.ComponentTest;
import org.jdragon.springz.test.controller.UserController;
import org.jdragon.springz.test.dao.CarDao;
import org.jdragon.springz.test.dao.UserDao;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.test.service.UserAddService;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.29 13:12
 * @Description:
 */
@Component
public class TestBean {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @AutowiredZ
    private ComponentTest componentTest;

    @AutowiredZ
    private UserController userController;

    @AutowiredZ
    private ImportTest importTest;

    @AutowiredZ
    @Qualifier("carOneDao")
    private CarDao carDao;

    @AutowiredZ
    private User user;

    @AutowiredZ
    @Qualifier("httpUser")
    private User httpUser;

    @AutowiredZ
    private UserDao userDao;

    @AutowiredZ
    private UserAddService userAddService;

    public void testBean() {

        logger.info("已注册bean列表", Arrays.toString(SpringzContext.getBeanDefinitionNames()));

        User userTest = SpringzContext.getBean(User.class);

        User userTest1 = (User) SpringzContext.getBean("user");

        logger.info(Optional.ofNullable(userTest).orElse(new User()).toString());

        logger.info(Optional.ofNullable(userTest1).orElse(new User()).toString());


        componentTest.test();

        componentTest.scopeTest();

        userController.save(user);

        userController.add(user);

        userController.saveAndAdd(user);

        carDao.resourceCar();

        carDao.qualifierCar();

        System.out.println(httpUser);



        System.out.println(userDao.toString());
    }
}
