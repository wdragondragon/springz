package org.jdragon.springz.test;

import org.jdragon.springz.ImportTest;
import org.jdragon.springz.core.IocContext;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.Inject;
import org.jdragon.springz.test.component.ComponentTest;
import org.jdragon.springz.test.controller.UserController;
import org.jdragon.springz.test.dao.CarDao;
import org.jdragon.springz.test.dao.UserDao;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.test.service.UserAddService;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

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

    @Inject
    private ComponentTest componentTest;

    @Inject
    private UserController userController;

    @Inject
    private ImportTest importTest;

    @Inject("carOneDao")
    private CarDao carDao;

    @Inject
    private User user;

    @Inject("httpUser")
    private User httpUser;

    @Inject
    private UserDao userDao;

    @Inject
    private UserAddService userAddService;

    public void testBean() {

        User userTest = IocContext.getBean(User.class);

        User userTest1 = (User) IocContext.getBean("user");

        logger.info(Optional.ofNullable(userTest).orElse(new User()).toString());

        logger.info(Optional.ofNullable(userTest1).orElse(new User()).toString());


        componentTest.test();

        componentTest.scopeTest();

        userController.save(user);

        userController.add(user);

        userController.saveAndAdd(user);

        carDao.resourceCar();

        carDao.qualifierCar();

        carDao.propertyCar();

        System.out.println(httpUser);

        System.out.println(userDao.toString());
    }
}
