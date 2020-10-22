package org.jdragon.springz.test;


import com.jdragon.common.response.normal.Result;
import org.jdragon.springz.annotation.core.*;
import org.jdragon.springz.annotation.feign.EnableFeignSpringZ;
import org.jdragon.springz.core.AnnotationApplicationContext;

import org.jdragon.springz.test.dao.Http;
import org.jdragon.springz.test.domain.RobotPostOrder;
import org.jdragon.springz.test.service.HttpTest;
import org.jdragon.springz.test.component.ComponentTest;
import org.jdragon.springz.test.controller.UserController;
import org.jdragon.springz.test.dao.CarDao;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.test.service.Robot;
import org.jdragon.springz.test.service.RobotResult;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;


import java.util.Arrays;
import java.util.List;

/**
 * @author 10619
 */
@SpringzMain
//@ComponentScans(value = {
//        @ComponentScan(basePackageClasses = App.class, includeFilters = {
//                @ComponentScan.ComponentFilter(type = FilterType.CUSTOM, classes = {MyFilter.class}),
//                @ComponentScan.ComponentFilter(type = FilterType.ANNOTATION
//                        , classes = {SpringzMain.class, Component.class, Service.class, Repository.class, Configuration.class, Controller.class}),
//        }
//                , useDefaultFilters = false)}
//)
//@Import(App.class)
//@ComponentScan(basePackage = {"org.jdragon.springz.test"})
@SpringzScan
@EnableFeignSpringZ
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

//    private static final AnnotationApplicationContext ctx = new AnnotationApplicationContext(App.class);
    private static final AnnotationApplicationContext ctx = new AnnotationApplicationContext(App.class);

    @AutowiredZ
    private static ComponentTest componentTest;

    @AutowiredZ
    private static UserController userController;

    @AutowiredZ
    @Qualifier("carOneDao")
    private static CarDao carDao;

    @AutowiredZ
    private static User user;

    @AutowiredZ
    private static HttpTest httpTest;

    @AutowiredZ
    private static Robot robot;

    @AutowiredZ
    private static RobotResult robotResult;

    public static void main(String[] args) {

//        testBean();

        testHttp();

        ctx.close();
    }

    public static void testHttp(){
//        httpTest.http();
//        httpTest.http1(1);
//        httpTest.http2(1);
//        httpTest.http4();
//        httpTest.http5(1);
//        httpTest.http6(1);
//        httpTest.http7(new Http(1));
        List<RobotPostOrder> postOrder = robot.getPostOrder();
        for (RobotPostOrder robotPostOrder : postOrder) {
            System.out.println(robotPostOrder);
        }
        Result<List<RobotPostOrder>> postOrder1 = robotResult.getPostOrder();
        System.out.println(postOrder1);
    }

    public static void testBean(){
        logger.info("已注册bean列表", Arrays.toString(ctx.getBeanDefinitionNames()));

        User userTest = ctx.getBean(User.class);

        User userTest1 = (User) ctx.getBean("user");

        logger.info(userTest.toString());

        logger.info(userTest1.toString());

        componentTest.test();

        componentTest.scopeTest();

        userController.save(user);

        userController.add(user);

        userController.saveAndAdd(user);

        carDao.resourceCar();

        carDao.qualifierCar();
    }
}
