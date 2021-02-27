package org.jdragon.springz.test;


import org.jdragon.springz.aop.annotation.EnableAop;
import org.jdragon.springz.core.annotation.*;
import org.jdragon.springz.core.IocContext;

import org.jdragon.springz.feign.annotation.EnableFeign;
import org.jdragon.springz.web.annotation.EnableWeb;

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

@IocMain
@IocScan
@EnableAop
@EnableWeb
@EnableFeign
public class App {

    @Inject
    private static TestBean testBean;

    @Inject
    private static TestFeign testFeign;

    @Inject
    private static TestScope testScope;

    @Inject
    private static TestWeb testWeb;

    @Inject
    private static TestProperty testProperty;

    public static void main(String[] args) {

        IocContext.run(App.class);

        testBean.testBean();

//        testFeign.testHttp();

        testScope.test();

        testProperty.test();

        testWeb.test();

        IocContext.close();
    }
}
