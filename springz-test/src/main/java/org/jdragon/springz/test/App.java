package org.jdragon.springz.test;



import org.jdragon.springz.core.AnnotationApplicationContext;
import org.jdragon.springz.core.annotation.*;
import org.jdragon.springz.core.annotation.ComponentScan.Filter;
import org.jdragon.springz.core.entry.FilterType;
import org.jdragon.springz.test.component.ComponentTest;
import org.jdragon.springz.test.component.MyFilter;
import org.jdragon.springz.test.controller.UserController;
import org.jdragon.springz.test.dao.CarDao;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;


import java.util.Arrays;

/**
 * @author 10619
 */
@SpringzMain
@ComponentScans(value = {
        @ComponentScan(basePackageClasses = App.class,includeFilters = {
                @Filter(type = FilterType.CUSTOM,classes = {MyFilter.class}),
                @Filter(type = FilterType.ANNOTATION
                        ,classes = {SpringzMain.class, Component.class, Service.class, Repository.class, Configuration.class, Controller.class}),
        }
        ,useDefaultFilters = false)}
)
//@ComponentScan(basePackageClasses = App.class)
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
        logger.info("已注册bean列表",Arrays.toString(ctx.getBeanDefinitionNames()));

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
