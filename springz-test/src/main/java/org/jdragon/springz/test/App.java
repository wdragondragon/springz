package org.jdragon.springz.test;



import org.jdragon.springz.core.AnnotationApplicationContext;
import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.ComponentScan;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.core.annotation.SpringzMain;
import org.jdragon.springz.test.component.ComponentTest;
import org.jdragon.springz.test.controller.UserController;
import org.jdragon.springz.test.dao.CarDao;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.utils.LogBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author 10619
 */
@SpringzMain
@ComponentScan(basePackageClasses = App.class)
public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    private static AnnotationApplicationContext ctx = new AnnotationApplicationContext(App.class);

    @Autowired
    private static ComponentTest componentTest;

    @Autowired
    private static UserController userController;

    @Autowired
    @Qualifier("carOneDao")
    private static CarDao carDao;

    @Autowired
    private static User user;

    public static void main(String[] args) {
        logger.info(LogBuilder.build("已注册bean列表",Arrays.toString(ctx.getBeanDefinitionNames())));

        componentTest.test();

        componentTest.scopeTest();

        userController.save(user);

        userController.add(user);

        userController.saveAndAdd(user);

        carDao.resourceCar();

        carDao.qualifierCar();

        ctx.close();
    }
}
