package org.jdragon.springz.test;


import org.jdragon.springz.aop.annotation.EnableAopSpringZ;
import org.jdragon.springz.core.annotation.*;
import org.jdragon.springz.core.SpringzContext;

import org.jdragon.springz.feign.annotation.EnableFeignSpringZ;
import org.jdragon.springz.web.annotation.EnableWebSpringZ;

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
@EnableAopSpringZ
@EnableWebSpringZ
@EnableFeignSpringZ
public class App {

    @AutowiredZ
    private static TestBean testBean;

    @AutowiredZ
    private static TestFeign testFeign;

    @AutowiredZ
    private static TestScope testScope;

    @AutowiredZ
    private static TestWeb testWeb;

    @AutowiredZ
    private static TestProperty testProperty;

    public static void main(String[] args) {

        SpringzContext.run(App.class);

        testBean.testBean();

//        testFeign.testHttp();

        testScope.test();

        testProperty.test();

        testWeb.test();

        SpringzContext.close();
    }
}
