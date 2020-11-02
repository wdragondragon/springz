package org.jdragon.springz.test;


import org.jdragon.springz.aop.annotation.EnableAopSpringZ;
import org.jdragon.springz.core.annotation.*;
import org.jdragon.springz.core.AnnotationApplicationContext;

import org.jdragon.springz.feign.annotation.EnableFeignSpringZ;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;


import java.util.Arrays;

/**
 * @author 10619
 */
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

@SpringzMain
@SpringzScan
@EnableFeignSpringZ
@EnableAopSpringZ
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    //    private static final AnnotationApplicationContext ctx = new AnnotationApplicationContext(App.class);
    private static final AnnotationApplicationContext ctx = new AnnotationApplicationContext(App.class);

    @AutowiredZ
    private static TestBean testBean;

    @AutowiredZ
    private static TestFeign testFeign;


    public static void main(String[] args) {

        logger.info("已注册bean列表", Arrays.toString(ctx.getBeanDefinitionNames()));

        User userTest = ctx.getBean(User.class);

        User userTest1 = (User) ctx.getBean("user");

        logger.info(userTest.toString());

        logger.info(userTest1.toString());

        testBean.testBean();

        testFeign.testHttp();

        ctx.close();
    }


}
